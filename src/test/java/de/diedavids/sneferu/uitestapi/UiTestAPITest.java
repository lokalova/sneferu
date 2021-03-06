package de.diedavids.sneferu.uitestapi;

import static de.diedavids.sneferu.ComponentDescriptors.button;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.Screens.OpenedScreens;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.web.app.main.MainScreen;
import com.haulmont.cuba.web.testsupport.TestUiEnvironment;
import de.diedavids.sneferu.CubaWebUiTestAPI;
import de.diedavids.sneferu.ScreenNotOpenException;
import de.diedavids.sneferu.UiTestAPI;
import de.diedavids.sneferu.example.Customer;
import de.diedavids.sneferu.example.CustomerStandardEditor;
import de.diedavids.sneferu.example.CustomerStandardLookup;
import de.diedavids.sneferu.screen.StandardEditorTestAPI;
import de.diedavids.sneferu.screen.StandardLookupTestAPI;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;

class UiTestAPITest {

  private UiTestAPI sut;
  private OpenedScreens openedScreens;

  @BeforeEach
  void setUp() {
    TestUiEnvironment environment = mock(TestUiEnvironment.class);

    Screens screens = mock(Screens.class);
    when(environment.getScreens()).thenReturn(screens);

    openedScreens = mock(OpenedScreens.class);
    when(screens.getOpenedScreens()).thenReturn(openedScreens);

    final ScreenBuilders screenBuilders = mock(ScreenBuilders.class);
    sut = new CubaWebUiTestAPI(
        environment,
        screenBuilders,
        MainScreen.class
    );
  }

  @Nested
  class OpenedInputDialog {


    @Test
    public void given_noInputDialogIsOpen_when_openedInputDialogIsRequested_thenScreenNotFoundExceptionIsRaised() {

      // given:
      dialogScreens()
          .thenReturn(
              noOpenScreens()
          );
      // when:

      ScreenNotOpenException exception = assertThrows(ScreenNotOpenException.class, () -> {
        sut.openedInputDialog();
      });

      // then:
      assertThat(exception).isNotNull();
    }
  }

  @Test
  void given_screenIsNotOpen_when_aLazyOpenedStandardEditor_anInstanceIsReturnedThatTriesToFetchTheScreenOnTheFly_whenRequestingScreen() {

    // given:
    final CustomerStandardEditor foundCustomerEditor = new CustomerStandardEditor();

    allScreens()
        .thenReturn(
            noOpenScreens()
        )
        .thenReturn(
            openScreens(foundCustomerEditor)
        );

    // expect:
    assertThat(lazyOpenedEditorScreen().screen())
        .isEqualTo(foundCustomerEditor);
  }

  @Test
  void given_screenIsNotOpen_when_aLazyOpenedStandardEditor_anInstanceIsReturnedThatTriesToFetchTheScreenOnTheFly_whenRequestingComponent() {

    // given:
    final CustomerStandardEditor foundCustomerEditor = mock(CustomerStandardEditor.class);

    // and:
    final Window screenWindow = mock(Window.class);
    when(foundCustomerEditor.getWindow())
        .thenReturn(screenWindow);

    // and:
    final Button okBtn = mock(Button.class);
    when(screenWindow.getComponentNN("okBtn"))
        .thenReturn(okBtn);

    allScreens()
        .thenReturn(
            noOpenScreens()
        )
        .thenReturn(
            openScreens(foundCustomerEditor)
        );

    // expect:
    assertThat(lazyOpenedEditorScreen().rawComponent(button("okBtn")))
        .isEqualTo(okBtn);
  }

  @Test
  void given_screenIsNotOpen_when_aLazyOpenedStandardLookup_anInstanceIsReturnedThatTriesToFetchTheScreenOnTheFly_whenRequestingComponent() {

    // given:
    final CustomerStandardLookup foundCustomerLookup = mock(CustomerStandardLookup.class);

    // and:
    final Window screenWindow = mock(Window.class);
    when(foundCustomerLookup.getWindow())
        .thenReturn(screenWindow);

    // and:
    final Button okBtn = mock(Button.class);
    when(screenWindow.getComponentNN("okBtn"))
        .thenReturn(okBtn);

    allScreens()
        .thenReturn(
            noOpenScreens()
        )
        .thenReturn(
            openScreens(foundCustomerLookup)
        );

    // expect:
    assertThat(lazyOpenedStandardLookupScreen().rawComponent(button("okBtn")))
        .isEqualTo(okBtn);
  }

  private StandardEditorTestAPI<Customer, CustomerStandardEditor> lazyOpenedEditorScreen() {
    return sut
        .getLazyOpenedEditorScreen(CustomerStandardEditor.class);
  }

  private StandardLookupTestAPI<Customer, CustomerStandardLookup> lazyOpenedStandardLookupScreen() {
    return sut
        .getLazyOpenedLookupScreen(CustomerStandardLookup.class);
  }


  @Test
  void given_screenIsOpen_when_aLazyOpenedStandardEditor_anInstanceIsReturned() {

    // given:
    final CustomerStandardEditor foundCustomerEditor = new CustomerStandardEditor();

    allScreens()
        .thenReturn(
            openScreens(foundCustomerEditor)
        );

    // expect:
    assertThat(lazyOpenedEditorScreen().screen())
        .isEqualTo(foundCustomerEditor);
  }

  private OngoingStubbing<Collection<Screen>> allScreens() {
    return when(openedScreens.getAll());
  }


  private OngoingStubbing<Collection<Screen>> dialogScreens() {
    return when(openedScreens.getDialogScreens());
  }


  private List<Screen> openScreens(Screen screen) {
    return Collections.singletonList(
        screen
    );
  }

  private List<Screen> noOpenScreens() {
    return Collections.emptyList();
  }
}

