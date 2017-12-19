package nl.yogh.aerius.wui.builder.ui.pulls;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.aerius.wui.builder.commands.PullRequestRetrievalActivationCommand;
import nl.yogh.aerius.wui.builder.commands.PullRequestRetrievalDeactivationCommand;
import nl.yogh.aerius.wui.builder.commands.PullRequestRetrievalEvent;
import nl.yogh.aerius.wui.builder.commands.PullRequestStatusInfoChangedEvent;
import nl.yogh.aerius.wui.builder.ui.pulls.PullRequestView.Presenter;
import nl.yogh.gwt.wui.activity.EventActivity;

public class PullRequestActivity extends EventActivity<Presenter, PullRequestView> implements Presenter {
  interface PullRequestActivityEventBinder extends EventBinder<PullRequestActivity> {}

  private final PullRequestActivityEventBinder EVENT_BINDER = GWT.create(PullRequestActivityEventBinder.class);

  private final HashMap<String, PullRequestInfo> pullRequestContentIndex = new HashMap<>();

  @Inject
  public PullRequestActivity(final PullRequestView view) {
    super(view);
  }

  @Override
  protected void onStart() {
    eventBus.fireEvent(new PullRequestRetrievalActivationCommand());
  }

  @Override
  public void onStop() {
    eventBus.fireEvent(new PullRequestRetrievalDeactivationCommand());
  }

  @EventHandler
  public void onPullRequestRetrievalEvent(final PullRequestRetrievalEvent e) {
    for (final PullRequestInfo info : e.getValue()) {
      eventBus.fireEvent(new PullRequestStatusInfoChangedEvent(info));
      handlePullRequestInfo(info);
    }

    final Collection<String> leftOverCol = e.getValue().stream().map(v -> v.hash()).collect(Collectors.toList());
    leftOverCol.removeAll(pullRequestContentIndex.keySet());

    for (final String leftOver : leftOverCol) {
      view.removePullRequest(leftOver);
    }

  }

  private void handlePullRequestInfo(final PullRequestInfo info) {
    final String idx = info.idx();

    if (!pullRequestContentIndex.containsKey(idx)) {
      view.insertPullRequest(info);
      pullRequestContentIndex.put(idx, info);
    }
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus);

    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }

  @Override
  public Presenter getPresenter() {
    return this;
  }
}
