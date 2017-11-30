package nl.yogh.aerius.wui;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.ImplementedBy;

import nl.yogh.aerius.wui.RootPanelFactory.RootPanelFactoryImpl;

@ImplementedBy(RootPanelFactoryImpl.class)
public interface RootPanelFactory {
  public class RootPanelFactoryImpl implements RootPanelFactory {
    @Override
    public Panel getPanel() {
      return RootPanel.get();
    }
  }

  Panel getPanel();
}
