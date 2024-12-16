package fp.players.info.view

import fp.players.info.MainApp
import scalafx.scene.control.{Label, TextField}
import scalafxml.core.macros.sfxml

@sfxml
class LoginController(private val userName: TextField,
                      private val passWord: TextField,
                      private val loginStatus: Label){
  def login(): Unit = {
    if (userName.getText().equals("User") && passWord.getText().equals("Pass1234")) {
      MainApp.showDatahub()
    }
    else {
      loginStatus.setText("Login Failed")
    }
  }
}