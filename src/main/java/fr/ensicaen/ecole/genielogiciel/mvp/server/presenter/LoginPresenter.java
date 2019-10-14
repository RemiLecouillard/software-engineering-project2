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

import fr.ensicaen.ecole.genielogiciel.mvp.client.view.IView;
import fr.ensicaen.ecole.genielogiciel.mvp.server.model.LoginModel;
import fr.ensicaen.ecole.genielogiciel.mvp.server.model.LoginService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public final class LoginPresenter implements IPresenter {

    private final LoginService _loginService;
    private LoginModel _model;

    public LoginPresenter() {
        _loginService = new LoginService();
    }

    public LoginPresenter( LoginService loginService ) {
        _loginService = loginService;
    }

    @Override
    public void setModel( LoginModel model ) {
        _model = model;
    }

    @Override
    public void addView( IView view ) {
    }

    @Override
    public void sayHello() {
    }

    public void addView( IView view, Socket socket ) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        Thread inputThread = new Thread(new Reception(view, input));
        inputThread.start();
    }

    void sayHello( IView view, String input ) {
        String message = _loginService.sayHello(input);
        _model.setMessage(message);
        view.setMessage(message);
    }

    final class Reception implements Runnable {

        private final IView _view;
        private final BufferedReader _input;

        Reception( IView view, BufferedReader input ) {
            _view = view;
            _input = input;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    String message = _input.readLine();
                    if (message == null) {
                        System.out.println("Connexion rompue : abandon du jeu");
                        System.exit(1);
                    } else {
                        System.out.println("Presenter : j'ai reçu" + message);
                        sayHello(_view, message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
