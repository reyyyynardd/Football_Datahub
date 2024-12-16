package fp.players.info.view

import fp.players.info.model.Player
import fp.players.info.MainApp
import fp.players.info.util.DateUtil.{DateFormater, StringFormater}
import scalafx.scene.control.{Alert, Label, TableColumn, TextField}
import scalafxml.core.macros.sfxml
import scalafx.stage.Stage
import scalafx.Includes._
import scalafx.event.ActionEvent

@sfxml
class EditPlayerController(private val fullNameField : TextField,
                           private val playerTeamField : TextField,
                           private val ageField : TextField,
                           private val positionField : TextField,
                           private val marketValueField : TextField,
                           private val dateOfBirthField : TextField) {

  var dialogStage: Stage  = null
  private var _player: Player = null
  var confirmClicked = false

  def player = _player
  def player_= (x : Player) {
    _player = x
    fullNameField.text = _player.fullName.value
    playerTeamField.text  = _player.playerTeam.value
    ageField.text = _player.age.value.toString
    positionField.text = _player.position.value
    marketValueField.text = _player.marketValue.value
    dateOfBirthField.text  = _player.dateOfBirth.value.asString
    dateOfBirthField.setPromptText("dd/mm/yyyy");
  }

  def handleConfirm(action :ActionEvent){
    if (isInputValid()) {
      _player.fullName <== fullNameField.text
      _player.playerTeam <== playerTeamField.text
      _player.age.value = ageField.getText().toInt
      _player.position <== positionField.text
      _player.marketValue <== marketValueField.text
      _player.dateOfBirth.value = dateOfBirthField.text.value.parseLocalDate;

      confirmClicked = true;
      dialogStage.close()
    }
  }

  def handleCancel(action :ActionEvent) {
    dialogStage.close();
  }
  def nullChecking (x : String) = x == null || x.length == 0
  def isInputValid() : Boolean = {
    var errorMessage = ""
    if (nullChecking(fullNameField.text.value))
      errorMessage += "Not a valid Name!\n"
    if (nullChecking(playerTeamField.text.value))
      errorMessage += "Not a valid Team!\n"
    if (nullChecking(ageField.text.value))
      errorMessage += "Not a valid Age!\n"
    else {
      try {
        Integer.parseInt(ageField.getText());
      } catch {
        case e: NumberFormatException =>
          errorMessage += "Age must be a Number! (Integer)\n"
      }
    }
    if (nullChecking(positionField.text.value))
      errorMessage += "Not a valid Position!\n"
    if (nullChecking(marketValueField.text.value))
      errorMessage += "Not a valid Market Value!\n"
    if (nullChecking(dateOfBirthField.text.value))
      errorMessage += "Not a valid Date of Birth!\n"
    else {
      if (!dateOfBirthField.text.value.isValid) {
        errorMessage += "Use Format DD/MM/YYYY!\n";
      }
    }

    if (errorMessage.length() == 0) {
      return true;
    } else {
      val alert = new Alert(Alert.AlertType.Error){
        initOwner(dialogStage)
        title = "Invalid Edits"
        headerText = "Please correct invalid fields"
        contentText = errorMessage
      }.showAndWait()

      return false;
    }
  }
}
