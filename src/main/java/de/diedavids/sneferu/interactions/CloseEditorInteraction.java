package de.diedavids.sneferu.interactions;

import com.haulmont.cuba.gui.screen.StandardEditor;
import com.haulmont.cuba.gui.util.OperationResult;
import de.diedavids.sneferu.screen.StandardEditorTestAPI;
import de.diedavids.sneferu.InteractionWithOutcome;

public class CloseEditorInteraction implements InteractionWithOutcome<OperationResult, StandardEditorTestAPI> {

    @Override
    public OperationResult execute(StandardEditorTestAPI screenTestAPI) {
        return ((StandardEditor) screenTestAPI.screen()).closeWithCommit();
    }

}
