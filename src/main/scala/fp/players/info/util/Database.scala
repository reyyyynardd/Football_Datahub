package fp.players.info.util

import scalikejdbc._
import fp.players.info.model.Player
import fp.players.info.model.Team

trait Database {
  val derbyDriverClassname = "org.apache.derby.jdbc.EmbeddedDriver"
  val dbURL = "jdbc:derby:myDB;create=true;";
  Class.forName(derbyDriverClassname)
  ConnectionPool.singleton(dbURL, "User", "Pass1234")
  implicit val session = AutoSession
}

object Database extends Database {
  def setupDB() = {
    if (!hasDBInitialize) {
      Player.initializeTable()
      Team.initializeTable()
    }
  }

  def hasDBInitialize: Boolean = {
    DB.getTable("Player")  match {
      case Some(x) => true
      case None => false
    }
    DB.getTable("Team")  match {
      case Some(x) => true
      case None => false
    }
  }
}
