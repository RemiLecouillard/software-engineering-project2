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
import fr.ensicaen.ecole.genielogiciel.mvp.server.presenter.LoginPresenter;
import fr.ensicaen.ecole.genielogiciel.mvp.server.view.LoginProxyView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ResourceBundle;

public final class ServerMain {

    public static final int PORT_NUMBER = 2017;

    public static void main( String[] args ) {
        new ServerMain().startServer(PORT_NUMBER);
    }

    public static ResourceBundle getMessageBundle() {
        return ResourceBundle.getBundle("fr.ensicaen.ecole.genielogiciel.mvp.MessageBundle");
    }

    private void startServer( int port ) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Le serveur est à l'écoute du port : " + port + " et attend les clients.");
            LoginPresenter presenter = new LoginPresenter();
            LoginModel model = new LoginModel();
            presenter.setModel(model);
            Thread thread = new Thread(new ClientsConnection(serverSocket, presenter));
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final class ClientsConnection implements Runnable {

        private final ServerSocket _serverSocket;
        private final LoginPresenter _presenter;

        ClientsConnection( ServerSocket serverSocket, LoginPresenter presenter ) {
            _serverSocket = serverSocket;
            _presenter = presenter;
        }

        @Override
        public void run() {
            try {
                // Accept only 2 clients for demonstration.
                for (int i = 0; i < 2; i++) {
                    Socket socket = _serverSocket.accept();
                    System.out.println("Serveur : un client vient de se connecter.");
                    IView view = new LoginProxyView(socket);
                    _presenter.addView(view, socket);
                    System.out.println("Serveur : le jeu peut commencer");
                }
            } catch (IOException e) {
                System.err.println("Erreur serveur: " + e.getMessage());
            }
        }
    }
}
