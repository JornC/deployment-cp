package nl.yogh.aerius.wui.builder.ui.landing;

import java.util.ArrayList;
import java.util.function.Consumer;

import com.google.inject.ImplementedBy;

import nl.yogh.aerius.builder.domain.DockerContainer;
import nl.yogh.aerius.builder.domain.DockerImage;
import nl.yogh.aerius.wui.builder.ui.landing.DockerView.Presenter;
import nl.yogh.gwt.wui.activity.View;

@ImplementedBy(DockerViewImpl.class)
public interface DockerView extends View<Presenter> {
  public interface Presenter {
    void stopAllContainers();

    void removeAllContainers();

    void removeAllImages();

    void removeImage(DockerImage info, Consumer<Boolean> callback);

    void stopContainer(DockerContainer info, Consumer<Boolean> callback);

    void removeContainer(DockerContainer info, Consumer<Boolean> callback);

    void purgeTracker();
  }

  void setImages(ArrayList<DockerImage> images);

  void setContainers(ArrayList<DockerContainer> containers);
}
