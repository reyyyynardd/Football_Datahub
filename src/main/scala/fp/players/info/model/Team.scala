package fp.players.info.model

import scalafx.beans.property.{StringProperty, IntegerProperty, ObjectProperty}
import java.time.LocalDate
import fp.players.info.util.Database
import fp.players.info.util.DateUtil._
import scalikejdbc._
import scala.util.{Try, Success, Failure}

class Team(val teamNameS: String, val leagueNameS: String) extends Database {
  def this() = this(null, null)

  var teamName = new StringProperty(teamNameS)
  var leagueName = new StringProperty(leagueNameS)
  var yearFounded = IntegerProperty(1905)
  var stadiumName = new StringProperty("Stamford Bridge")

  def save(): Try[Int] = {
    if (!(isExist)) {
      Try(DB autoCommit { implicit session =>
        sql"""
          insert into Team (teamName, leagueName, yearFounded, stadiumName) values
            (${teamName.value}, ${leagueName.value}, ${yearFounded.value},
              ${stadiumName.value})
        """.update.apply()
      })
    } else {
      Try(DB autoCommit { implicit session =>
        sql"""
        update Team
        set
        teamName  = ${teamName.value} ,
        leagueName   = ${leagueName.value},
        yearFounded     = ${yearFounded.value},
        stadiumName = ${stadiumName.value},
         where teamName = ${teamName.value} and
         leagueName = ${leagueName.value}
        """.update.apply()
      })
    }

  }

  def delete(): Try[Int] = {
    if (isExist) {
      Try(DB autoCommit { implicit session =>
        sql"""
        delete from Team where
          teamName = ${teamName.value} and leagueName = ${leagueName.value}
        """.update.apply()
      })
    } else
      throw new Exception("Team does not Exists in Database")
  }

  def isExist: Boolean = {
    DB readOnly { implicit session =>
      sql"""
        select * from Team where
        teamName = ${teamName.value} and leagueName = ${leagueName.value}
      """.map(rs => rs.string("teamName")).single.apply()
    } match {
      case Some(x) => true
      case None => false
    }

  }
}

object Team extends Database {
  def apply(teamNameS: String,
            leagueNameS: String,
            yearFoundedI: Int,
            stadiumNameS: String,
           ): Team = {

    new Team(teamNameS, leagueNameS) {
      yearFounded.value = yearFoundedI
      stadiumName.value = stadiumNameS
    }

  }

  def initializeTable() = {
    DB autoCommit { implicit session =>
      sql"""
      create table Team (
        id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
        teamName varchar(64),
        leagueName varchar(64),
        yearFounded int,
        stadiumName varchar(100)
      )
      """.execute.apply()
    }
  }

  def getAllTeams: List[Team] = {
    DB readOnly { implicit session =>
      sql"select * from Team".map(rs => Team(rs.string("teamName"),
        rs.string("leagueName"), rs.int("yearFounded"),
        rs.string("stadiumName"))).list.apply()
    }
  }
}


