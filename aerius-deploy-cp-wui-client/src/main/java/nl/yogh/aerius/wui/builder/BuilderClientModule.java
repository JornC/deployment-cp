package nl.yogh.aerius.wui.builder;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.gwt.place.shared.PlaceHistoryHandler.Historian;

import nl.yogh.aerius.wui.builder.component.BuilderPlaceNavigation;
import nl.yogh.aerius.wui.builder.component.PlaceNavigation;
import nl.yogh.aerius.wui.builder.daemons.BuilderDaemonBootstrapper;
import nl.yogh.aerius.wui.builder.dev.BuilderDevelopmentObserver;
import nl.yogh.aerius.wui.builder.history.BuilderPlaceHistoryMapper;
import nl.yogh.aerius.wui.builder.place.LandingPlace;
import nl.yogh.gwt.wui.activity.ActivityMapper;
import nl.yogh.gwt.wui.daemon.DaemonBootstrapper;
import nl.yogh.gwt.wui.dev.DevelopmentObserver;
import nl.yogh.gwt.wui.history.HTML5Historian;
import nl.yogh.gwt.wui.history.PlaceHistoryMapper;
import nl.yogh.gwt.wui.place.ApplicationPlace;
import nl.yogh.gwt.wui.place.DefaultPlace;

public class BuilderClientModule extends AbstractGinModule {
  @Override
  protected void configure() {
    bind(ApplicationPlace.class).annotatedWith(DefaultPlace.class).to(LandingPlace.class);
    bind(Historian.class).to(HTML5Historian.class);

    // Bind components
    bind(PlaceNavigation.class).to(BuilderPlaceNavigation.class);

    bind(ActivityMapper.class).to(BuilderActivityMapper.class);
    bind(PlaceHistoryMapper.class).to(BuilderPlaceHistoryMapper.class);
    bind(DaemonBootstrapper.class).to(BuilderDaemonBootstrapper.class);
    bind(DevelopmentObserver.class).to(BuilderDevelopmentObserver.class);

    install(new GinFactoryModuleBuilder().build(BuilderActivityFactory.class));
  }
}
