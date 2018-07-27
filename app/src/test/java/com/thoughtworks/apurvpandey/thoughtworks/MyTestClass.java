package com.thoughtworks.apurvpandey.thoughtworks;

import com.thoughtworks.apurvpandey.thoughtworks.homeScreen.HomeScreenActivity;
import com.thoughtworks.apurvpandey.thoughtworks.homeScreen.HomeScreenContract;
import com.thoughtworks.apurvpandey.thoughtworks.homeScreen.HomeScreenInteractor;
import com.thoughtworks.apurvpandey.thoughtworks.homeScreen.HomeScreenPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MyTestClass {

    @Mock
    HomeScreenContract contract;

    @Mock
    HomeScreenInteractor interactor;

    private HomeScreenPresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new HomeScreenPresenter(contract, interactor);
    }

    @Test
    public void checkDialogVisibility() {
        presenter.showDialog(HomeScreenActivity.DialogType.FILTER);                                             //Explicitily show Progress Dailog
        verify(contract, times(1)).showDialog(HomeScreenActivity.DialogType.FILTER);    //Verifying if progress Dialog was displayed more than once.
    }


}
