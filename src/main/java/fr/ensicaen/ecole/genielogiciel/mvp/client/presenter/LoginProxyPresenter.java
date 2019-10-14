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

package fr.ensicaen.ecole.genielogiciel.mvp.client.presenter;

import fr.ensicaen.ecole.genielogiciel.mvp.client.view.IView;
import fr.ensicaen.ecole.genielogiciel.mvp.server.model.LoginModel;
import fr.ensicaen.ecole.genielogiciel.mvp.server.presenter.IPresenter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public final class LoginProxyPresenter implements IPresenter {

    private IView _view;
    private PrintWriter _output;

    public LoginProxyPresenter( Socket socket ) throws IOException {
        _output = new PrintWriter(socket.getOutputStream());
    }

    @Override
    public void setModel( LoginModel model ) {
    }

    @Override
    public void addView( IView view ) {
        _view = view;
    }

    @Override
    public void sayHello() {
        String input = _view.getInput();
        sendMessage(input);
    }

    private void sendMessage( String message ) {
        _output.println(message);
        System.out.println("Proxy presenter: j'ai envoyé : " + message);
        _output.flush();
    }
}