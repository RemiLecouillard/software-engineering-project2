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

package fr.ensicaen.ecole.genielogiciel.mvp.client.view;

import fr.ensicaen.ecole.genielogiciel.mvp.server.presenter.IPresenter;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public final class LoginView implements IView {

    private IPresenter _presenter;
    @FXML
    private TextField _inputTextfield;
    @FXML
    private Label _message;
    private BufferedReader _input;

    @Override
    public void setPresenter( IPresenter presenter ) {
        _presenter = presenter;
    }

    @Override
    public String getInput() {
        return _inputTextfield.getText();
    }

    @Override
    public void setMessage( String text ) {
        System.out.println("Login View text = " + text);
        Platform.runLater(() -> _message.setText(text));
    }

    void setProxy( Socket socket ) throws IOException {
        _input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        Thread inputThread = new Thread(new Reception());
        inputThread.start();
    }

    @FXML
    private void handleSayHello( ActionEvent event ) {
        // Delegate to the controller.
        // Do not send Javafx components.
        _presenter.sayHello();
    }

    final class Reception implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    String text = _input.readLine();
                    if (text == null) {
                        System.out.println("Connexion rompue : abandon");
                        System.exit(1);
                    } else {
                        System.out.println("View: j'ai reçu " + text);
                        setMessage(text);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
