package nl.yogh.aerius.wui.builder.ui.landing;

import java.util.function.Consumer;

import javax.inject.Inject;

import com.google.gwt.user.client.Window;

import nl.yogh.aerius.builder.domain.DockerContainer;
import nl.yogh.aerius.builder.domain.DockerImage;
import nl.yogh.aerius.builder.service.DockerManagementServiceAsync;
import nl.yogh.aerius.wui.builder.ui.landing.DockerView.Presenter;
import nl.yogh.gwt.wui.activity.EventActivity;
import nl.yogh.gwt.wui.service.AppAsyncCallback;

public class DockerActivity extends EventActivity<Presenter, DockerView> implements Presenter {
  private final DockerManagementServiceAsync service;

  @Inject
  public DockerActivity(final DockerView view, final DockerManagementServiceAsync dockerManagementService) {
    super(view);
    this.service = dockerManagementService;
  }

  @Override
  protected void onStart() {
    retrieveImages();
    retrieveContainers();
  }

  @Override
  public Presenter getPresenter() {
    return this;
  }

  @Override
  public void stopAllContainers() {
    service.stopAllContainers(AppAsyncCallback.create(v -> retrieveContainers()));
  }

  @Override
  public void removeAllContainers() {
    service.removeAllContainers(AppAsyncCallback.create(v -> retrieveContainers()));
  }

  @Override
  public void removeAllImages() {
    if (Window.confirm("Are you sure you want to delete all images?")) {
      service.removeAllImages(AppAsyncCallback.create(v -> retrieveImages()));
    }
  }

  private void retrieveContainers() {
    service.retrieveContainers(AppAsyncCallback.create(view::setContainers));
  }

  private void retrieveImages() {
    service.retrieveImages(AppAsyncCallback.create(view::setImages));
  }

  @Override
  public void stopContainer(final DockerContainer info, final Consumer<Boolean> callback) {
    service.stopContainer(info, AppAsyncCallback.create(callback));
  }

  @Override
  public void removeContainer(final DockerContainer info, final Consumer<Boolean> callback) {
    service.removeContainer(info, AppAsyncCallback.create(callback));
  }

  @Override
  public void removeImage(final DockerImage info, final Consumer<Boolean> callback) {
    service.removeImage(info, AppAsyncCallback.create(callback));
  }
}
