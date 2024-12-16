package fp.players.info.view

import fp.players.info.model.Team
import fp.players.info.MainApp
import scalafx.scene.control.{Alert, Label, TableColumn, TextField}
import scalafxml.core.macros.sfxml
import scalafx.stage.Stage
import scalafx.Includes._
import scalafx.event.ActionEvent

@sfxml
class EditTeamController(private val teamNameField : TextField,
                         private val yearFoundedField : TextField,
                         private val leagueField : TextField,
                         private val stadiumField : TextField) {

  var dialogStage: Stage  = null
  private var _team: Team = null
  var confirmClicked = false

  def team = _team
  def team_= (x : Team) {
    _team = x
    teamNameField.text = _team.teamName.value
    yearFoundedField.text  = _team.yearFounded.value.toString
    leagueField.text = _team.leagueName.value
    stadiumField.text = _team.stadiumName.value
  }

  def handleConfirm(action :ActionEvent){
    if (isInputValid()) {
      _team.teamName <== teamNameField.text
      _team.yearFounded.value = yearFoundedField.getText().toInt
      _team.leagueName <== leagueField.text
      _team.stadiumName <== stadiumField.text

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
    if (nullChecking(teamNameField.text.value))
      errorMessage += "Not a valid Team Name!\n"
    if (nullChecking(yearFoundedField.text.value))
      errorMessage += "Not a valid Year!\n"
    else {
      try {
        Integer.parseInt(yearFoundedField.getText());
      } catch {
        case e: NumberFormatException =>
          errorMessage += "Year must be a Number! (Integer)\n"
      }
    }
    if (nullChecking(leagueField.text.value))
      errorMessage += "Not a valid League!\n"
    if (nullChecking(stadiumField.text.value))
      errorMessage += "Not a valid Stadium!\n"

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
