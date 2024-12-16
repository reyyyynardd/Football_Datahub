package fp.players.info.model

import scalafx.beans.property.{StringProperty, IntegerProperty, ObjectProperty}
import java.time.LocalDate
import fp.players.info.util.Database
import fp.players.info.util.DateUtil._
import scalikejdbc._
import scala.util.{Try, Success, Failure}

class Player(val fullNameS: String, val playerTeamS: String) extends Database {
  def this() = this(null, null)

  var fullName = new StringProperty(fullNameS)
  var playerTeam = new StringProperty(playerTeamS)
  var age = IntegerProperty(18)
  var position = new StringProperty("Position")
  var marketValue = new StringProperty("$50 million")
  var dateOfBirth = ObjectProperty[LocalDate](LocalDate.of(2004, 8, 12))

  def save(): Try[Int] = {
    if (!(isExist)) {
      Try(DB autoCommit { implicit session =>
        sql"""
          insert into Player (fullName, playerTeam,
            position, age, marketValue, dateOfBirth) values
            (${fullName.value}, ${playerTeam.value}, ${position.value},
              ${age.value},${marketValue.value}, ${dateOfBirth.value.asString})
        """.update.apply()
      })
    } else {
      Try(DB autoCommit { implicit session =>
        sql"""
        update Player
        set
        fullName  = ${fullName.value} ,
        playerTeam   = ${playerTeam.value},
        position     = ${position.value},
        age = ${age.value},
        marketValue       = ${marketValue.value},
        dateOfBirth       = ${dateOfBirth.value.asString}
         where fullName = ${fullName.value} and
         playerTeam = ${playerTeam.value}
        """.update.apply()
      })
    }

  }

  def delete(): Try[Int] = {
    if (isExist) {
      Try(DB autoCommit { implicit session =>
        sql"""
        delete from Player where
          fullName = ${fullName.value} and playerTeam = ${playerTeam.value}
        """.update.apply()
      })
    } else
      throw new Exception("Player not Exists in Database")
  }

  def isExist: Boolean = {
    DB readOnly { implicit session =>
      sql"""
        select * from Player where
        fullName = ${fullName.value} and playerTeam = ${playerTeam.value}
      """.map(rs => rs.string("fullName")).single.apply()
    } match {
      case Some(x) => true
      case None => false
    }

  }
}

object Player extends Database {
  def apply(fullNameS: String,
            playerTeamS: String,
            positionS: String,
            ageI: Int,
            marketValueS: String,
            dateOfBirthS: String
           ): Player = {

    new Player(fullNameS, playerTeamS) {
      position.value = positionS
      age.value = ageI
      marketValue.value = marketValueS
      dateOfBirth.value = dateOfBirthS.parseLocalDate
    }

  }

  def initializeTable() = {
    DB autoCommit { implicit session =>
      sql"""
      create table Player (
        id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        fullName varchar(64),
        playerTeam varchar(64),
        position varchar(200),
        age int,
        marketValue varchar(100),
        dateOfBirth varchar(64)
      )
      """.execute.apply()
    }
  }

  def getAllPlayers: List[Player] = {
    DB readOnly { implicit session =>
      sql"select * from Player".map(rs => Player(rs.string("fullName"),
        rs.string("playerTeam"), rs.string("position"),
        rs.int("age"), rs.string("marketValue"), rs.string("dateOfBirth"))).list.apply()
    }
  }
}


