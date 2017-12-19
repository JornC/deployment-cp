/*
 * Copyright Dutch Ministry of Economic Affairs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package nl.yogh.aerius.server.startup;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listener that is run during startup of application and can be used to initialize static variables.
 */
@WebListener
public class StartupContextListener implements ServletContextListener {
  private static final Logger LOG = LoggerFactory.getLogger(StartupContextListener.class);

  @Override
  public void contextInitialized(final ServletContextEvent event) {
    LOG.info("Initializing context..");

    ApplicationFactory.init(System.getProperties());

    LOG.info("Context initialized.");
  }

  @Override
  public void contextDestroyed(final ServletContextEvent event) {
    LOG.info("Shutting down context..");

    ApplicationFactory.shutdown();

    LOG.info("Context shut down.");
  }
}