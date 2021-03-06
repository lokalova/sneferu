package de.diedavids.sneferu.interactions;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.HasValue;
import de.diedavids.sneferu.InteractionWithOutcome;
import de.diedavids.sneferu.components.ComponentDescriptor;
import de.diedavids.sneferu.components.testapi.ComponentTestAPI;

import de.diedavids.sneferu.screen.StandardEditorTestAPI;

public class GetValueInteraction<T> implements InteractionWithOutcome<T, StandardEditorTestAPI> {

  private final ComponentDescriptor<? extends Component, ? extends ComponentTestAPI> componentDescriptor;

  public <C extends Component, F extends ComponentTestAPI<C>> GetValueInteraction(
      ComponentDescriptor<C, F> componentDescriptor
  ) {
    this.componentDescriptor = componentDescriptor;
  }

  @Override
  public T execute(StandardEditorTestAPI screenTestAPI) {
    HasValue hasValue = (HasValue) screenTestAPI.component(componentDescriptor).rawComponent();
    return (T) hasValue.getValue();
  }
}