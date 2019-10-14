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

import fr.ensicaen.ecole.genielogiciel.mvp.ServerMain;
import fr.ensicaen.ecole.genielogiciel.mvp.client.presenter.LoginProxyPresenter;
import fr.ensicaen.ecole.genielogiciel.mvp.server.presenter.IPresenter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

import static fr.ensicaen.ecole.genielogiciel.mvp.ServerMain.getMessageBundle;

public class ConnectionView {

    @FXML
    private TextField _ipAddressTextfield;

    @FXML
    public void checkIPAddress( ActionEvent actionEvent ) {
        // TODO: check _ipAddressTextfield.getText() consistency.
    }

    @FXML
    private void handleButtonStartAction( ActionEvent event ) throws IOException {
        Stage stage = getStage(event);
        stage.close();

        String ipAddress;
        if (_ipAddressTextfield.getLength() == 0) {
            ipAddress = "127.0.0.1";
        } else {
            ipAddress = _ipAddressTextfield.getText();
        }
        startClient(stage, ServerMain.PORT_NUMBER, ipAddress);
    }

    private Stage getStage( ActionEvent event ) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    private void startClient( Stage primaryStage, int port, String IPAddress ) throws IOException {
        primaryStage.setTitle(getMessageBundle().getString("login.title"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/ensicaen/ecole/genielogiciel/mvp/client/view/loginscreen.fxml"), getMessageBundle());
        Parent root = loader.load();
        // getController() ne retourne pas le controleur mais la Vue
        // si l'on veut que le contrôleur soit indépendant de la GUI.
        LoginView view = loader.getController();

        try {
            Socket socket = new Socket(IPAddress, port);
            IPresenter presenter = new LoginProxyPresenter(socket);
            view.setProxy(socket);

            view.setPresenter(presenter);
            presenter.addView(view);

            Scene scene = new Scene(root, 400, 120);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (ConnectException e) {
            // Connection refused
            e.printStackTrace();
        }
    }
}
