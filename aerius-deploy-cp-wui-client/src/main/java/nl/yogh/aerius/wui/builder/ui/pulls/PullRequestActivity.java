package nl.yogh.aerius.wui.builder.ui.pulls;

import java.util.HashMap;
import java.util.HashSet;

import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.aerius.wui.builder.commands.PullRequestRetrievalActivationCommand;
import nl.yogh.aerius.wui.builder.commands.PullRequestRetrievalDeactivationCommand;
import nl.yogh.aerius.wui.builder.commands.PullRequestRetrievalEvent;
import nl.yogh.aerius.wui.builder.ui.pulls.PullRequestView.Presenter;
import nl.yogh.gwt.wui.activity.EventActivity;

public class PullRequestActivity extends EventActivity<Presenter, PullRequestView> implements Presenter {
  interface PullRequestActivityEventBinder extends EventBinder<PullRequestActivity> {}

  private final PullRequestActivityEventBinder EVENT_BINDER = GWT.create(PullRequestActivityEventBinder.class);

  private final HashMap<String, String> pullRequestContentIndex = new HashMap<>();
  private final HashSet<String> pullRequestBusyIndex = new HashSet<>();

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
      handlePullRequestInfo(info);
    }
  }

  private void handlePullRequestInfo(final PullRequestInfo info) {
    final String idx = info.idx();

    // If the PR isn't known
    // OR If the PR has changed
    // OR it exists in the busy index and the PR is not busy
    // OR it does not exist in the busy index but the PR is busy
    if (!pullRequestContentIndex.containsKey(idx)
        || !pullRequestContentIndex.get(idx).equals(info.hash())
        || (pullRequestBusyIndex.contains(idx) && !info.isBusy())
        || (!pullRequestBusyIndex.contains(idx) && info.isBusy())) {
      view.insertOrUpdatePullRequest(info);
      pullRequestContentIndex.put(idx, info.hash());
    }

    if (info.isBusy()) {
      pullRequestBusyIndex.add(idx);
    } else {
      pullRequestBusyIndex.remove(idx);
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
