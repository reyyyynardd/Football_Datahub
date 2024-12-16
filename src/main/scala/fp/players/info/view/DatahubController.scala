package fp.players.info.view
import fp.players.info.model.{Player, Team}
import fp.players.info.MainApp
import scalafx.scene.control.{Alert, Label, TableColumn, TableView}
import scalafxml.core.macros.sfxml
import fp.players.info.util.DateUtil._
import scalafx.Includes._
import scalafx.beans.property.StringProperty
import scalafx.event.ActionEvent
import scalafx.scene.control.Alert.AlertType

@sfxml
class DatahubController(private val playerTable : TableView[Player],
                        private val fullNameColumn : TableColumn[Player, String],
                        private val playerTeamColumn : TableColumn[Player, String],
                        private val fullNameLabel : Label,
                        private val playerTeamLabel : Label,
                        private val ageLabel : Label,
                        private val positionLabel : Label,
                        private val marketValueLabel :  Label,
                        private val dateOfBirthLabel : Label,
                        private val teamTable : TableView[Team],
                        private val teamNameColumn: TableColumn[Team, String],
                        private val leagueNameColumn: TableColumn[Team, String],
                        private val teamNameLabel: Label,
                        private val yearLabel: Label,
                        private val leagueLabel: Label,
                        private val stadiumLabel: Label,
                              ) {

  playerTable.items = MainApp.playerData
  fullNameColumn.cellValueFactory = {
    _.value.fullName
  }
  playerTeamColumn.cellValueFactory = {
    _.value.playerTeam
  }

  teamTable.items = MainApp.teamData
  teamNameColumn.cellValueFactory = {
    _.value.teamName
  }
  leagueNameColumn.cellValueFactory = {
    _.value.leagueName
  }

  private def showPlayerInfo(player: Option[Player]) = {
    player match {
      case Some(player) =>

        fullNameLabel.text <== player.fullName
        playerTeamLabel.text <== player.playerTeam
        ageLabel.text = player.age.value.toString
        positionLabel.text <== player.position
        marketValueLabel.text <== player.marketValue
        dateOfBirthLabel.text = player.dateOfBirth.value.asString

      case None =>
        fullNameLabel.text.unbind()
        playerTeamLabel.text.unbind()
        ageLabel.text.unbind()
        marketValueLabel.text.unbind()
        positionLabel.text.unbind()

        fullNameLabel.text = ""
        playerTeamLabel.text = ""
        ageLabel.text = ""
        positionLabel.text = ""
        marketValueLabel.text = ""
        dateOfBirthLabel.text = ""
    }
  }
  showPlayerInfo(None)
  playerTable.selectionModel.value.selectedItem.onChange(
    (x, y, z) => {
      showPlayerInfo(Option(z))
    }
  )

  private def showTeamInfo(team: Option[Team]) = {
    team match {
      case Some(team) =>

        teamNameLabel.text <== team.teamName
        leagueLabel.text <== team.leagueName
        yearLabel.text = team.yearFounded.value.toString
        stadiumLabel.text <== team.stadiumName

      case None =>
        teamNameLabel.text.unbind()
        leagueLabel.text.unbind()
        yearLabel.text.unbind()
        stadiumLabel.text.unbind()

        teamNameLabel.text = ""
        leagueLabel.text = ""
        yearLabel.text = ""
        stadiumLabel.text = ""
    }
  }
  showTeamInfo(None)
  teamTable.selectionModel.value.selectedItem.onChange(
    (x, y, z) => {
      showTeamInfo(Option(z))
    }
  )

  def handleDeletePlayer(action: ActionEvent) = {
    val selectedIndex = playerTable.selectionModel().selectedIndex()
    if (selectedIndex >= 0) {
      MainApp.playerData.remove(selectedIndex).delete()
    }
    else {
      val alert = new Alert(AlertType.Warning){
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Player Selected"
        contentText = "Please select a Player from the table you wish to delete."
      }.showAndWait()
    }
  }

  def handleDeleteTeam(action: ActionEvent) = {
    val selectedIndex = teamTable.selectionModel().selectedIndex()
    if (selectedIndex >= 0) {
      MainApp.teamData.remove(selectedIndex).delete()
    }
    else {
      val alert = new Alert(AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Team Selected"
        contentText = "Please select a Team from the table you wish to delete."
      }.showAndWait()
    }
  }

  def handleNewPlayer(action: ActionEvent) = {
    val player = new Player("", "")
    val confirmClicked = MainApp.showEditPlayer(player);
    if (confirmClicked) {
      MainApp.playerData += player
      player.save()
    }
  }

  def handleEditPlayer(action: ActionEvent) = {
    val selectedPlayer = playerTable.selectionModel().selectedItem.value
    if (selectedPlayer != null) {
      val confirmClicked = MainApp.showEditPlayer(selectedPlayer)
      if (confirmClicked) {
        showPlayerInfo(Some(selectedPlayer))
        selectedPlayer.save()
      }
    } else {
      // Nothing selected.
      val alert = new Alert(Alert.AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Person Selected"
        contentText = "Please select a person in the table."
      }.showAndWait()
    }
  }

  def handleNewTeam(action: ActionEvent) = {
    val team = new Team("", "")
    val confirmClicked = MainApp.showEditTeam(team);
    if (confirmClicked) {
      MainApp.teamData += team
      team.save()
    }
  }

  def handleEditTeam(action: ActionEvent) = {
    val selectedTeam = teamTable.selectionModel().selectedItem.value
    if (selectedTeam != null) {
      val confirmClicked = MainApp.showEditTeam(selectedTeam)
      if (confirmClicked) {
        showTeamInfo(Some(selectedTeam))
        selectedTeam.save()
      }
    } else {
      // Nothing selected.
      val alert = new Alert(Alert.AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Team Selected"
        contentText = "Please select a person in the table."
      }.showAndWait()
    }
  }
}



