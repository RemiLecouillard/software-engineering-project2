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

package fr.ensicaen.ecole.genielogiciel.mvp;

import fr.ensicaen.ecole.genielogiciel.mvp.client.view.IView;
import fr.ensicaen.ecole.genielogiciel.mvp.server.model.LoginModel;
import fr.ensicaen.ecole.genielogiciel.mvp.server.model.LoginService;
import fr.ensicaen.ecole.genielogiciel.mvp.server.presenter.LoginPresenter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public final class LoginPresenterIT {

    private final LoginService _loginService = spy(new LoginService());
    private final LoginModel _model = spy(new LoginModel());
    @Parameterized.Parameter(value = 0)
    public String input;
    @Parameterized.Parameter(value = 1)
    public String output;
    @Mock
    private IView _view;

    @Parameterized.Parameters
    public static Collection<String[]> usernames() {
        return Arrays.asList(new String[][]{
                {"", "Bonjour inconnu !"},
                {"Toto", "Bonjour Toto"}});
    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        Locale.setDefault(new Locale("fr", "FR"));
    }

    @Test
    public void should_return_correct_login_message_when_a_name_is_given_or_not() {
        // given
        LoginPresenter presenter = new LoginPresenter(_loginService);
        presenter.setModel(_model);
        presenter.addView(_view);

        // then
        assertNull(_model.getMessage());
        when(_view.getInput()).thenReturn(input);

        // when
        presenter.sayHello();

        // then
        assertNull(_model.getMessage());
    }
}
