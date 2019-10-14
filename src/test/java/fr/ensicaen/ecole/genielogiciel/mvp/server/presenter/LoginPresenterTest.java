/*
 * Copyright (c)
 *  ENSICAEN, École publique d'ingénieurs et centres de recherche, Caen, FRANCE.
 *  https://www.ensicaen.fr
 *
 * This file is licenced under the MIT License.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package fr.ensicaen.ecole.genielogiciel.mvp.server.presenter;

import java.util.Arrays;
import java.util.Collection;

import fr.ensicaen.ecole.genielogiciel.mvp.client.view.IView;
import fr.ensicaen.ecole.genielogiciel.mvp.server.model.LoginModel;
import fr.ensicaen.ecole.genielogiciel.mvp.server.model.LoginService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class LoginPresenterTest {

    @Parameterized.Parameter(value = 0)
    public String input;
    @Parameterized.Parameter(value = 1)
    public String output;

    @Mock
    private IView _view;
    @Mock
    private LoginModel _model;
    @Mock
    private LoginService _loginService;

    @Parameterized.Parameters
    public static Collection<String[]> usernames() {
        return Arrays.asList(new String[][]{
                {"", "Bonjour inconnu !"},
                {"Test", "Bonjour Test"}});
    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_return_correct_message_when_given_a_name() {
        // given
        LoginPresenter presenter = new LoginPresenter(_loginService);
        presenter.setModel(_model);
        presenter.addView(_view);
        when(_view.getInput()).thenReturn(input);
        when(_loginService.sayHello(input)).thenReturn(output);

        // when
        presenter.sayHello(_view, input);

        // then
        verify(_loginService, times(1)).sayHello(input);
        verify(_model, times(1)).setMessage(output);
    }
}
