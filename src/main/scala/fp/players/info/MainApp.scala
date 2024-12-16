package fp.players.info

import fp.players.info.model.{Player, Team}
import fp.players.info.util.Database
import fp.players.info.view.{EditPlayerController, EditTeamController}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafxml.core.{FXMLLoader, FXMLView, NoDependencyResolver}
import javafx.{scene => jfxs}
import scalafx.collections.ObservableBuffer
import scalafx.scene.image.Image
import scalafx.stage.{Modality, Stage}

object MainApp extends JFXApp {
  Database.setupDB()

  val playerData = new ObservableBuffer[Player]()
  playerData ++= Player.getAllPlayers

  val teamData = new ObservableBuffer[Team]()
  teamData ++= Team.getAllTeams

  val rootResource = getClass.getResource("view/MenuLayout.fxml")
  val loader = new FXMLLoader(rootResource, NoDependencyResolver)
  loader.load();
  val roots = loader.getRoot[jfxs.layout.BorderPane]

  stage = new PrimaryStage {
    title = "Football Datahub"
    scene = new Scene {
      stylesheets += getClass.getResource("view/Design.css").toString
      root = roots
    }
    icons += new Image(getClass.getResourceAsStream("image/Football.png"))
  }

  def showDatahub() = {
    val resource = getClass.getResource("view/Datahub.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  def showLogin() = {
    val resource = getClass.getResource("view/Login.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  def showEditPlayer(player: Player): Boolean = {
    val resource = getClass.getResourceAsStream("view/EditPlayer.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource);
    val roots2  = loader.getRoot[jfxs.Parent]
    val control = loader.getController[EditPlayerController#Controller]
    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        stylesheets += getClass.getResource("view/Design.css").toString
        root = roots2
      }
    }
    control.dialogStage = dialog
    control.player = player
    dialog.showAndWait()
    control.confirmClicked
  }

  def showEditTeam(team: Team): Boolean = {
    val resource = getClass.getResourceAsStream("view/EditTeam.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource);
    val roots2 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[EditTeamController#Controller]
    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        stylesheets += getClass.getResource("view/Design.css").toString
        root = roots2
      }
    }
    control.dialogStage = dialog
    control.team = team
    dialog.showAndWait()
    control.confirmClicked
  }

  showLogin()
}
