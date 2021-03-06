package io.getquill.source.jdbc

import io.getquill._

class JdbcSourceSpec extends Spec {

  "probes sqls" - {
    val p = testDB.probe("DELETE FROM TestEntity")
  }

  "provides transaction support" - {
    "success" in {
      testDB.run(qr1.delete)
      testDB.transaction {
        testDB.run(qr1.insert(_.i -> 33))
      }
      testDB.run(qr1).map(_.i) mustEqual List(33)
    }
    "failure" in {
      testDB.run(qr1.delete)
      intercept[IllegalStateException] {
        testDB.transaction {
          testDB.run(qr1.insert(_.i -> 33))
          throw new IllegalStateException
        }
      }
      testDB.run(qr1).isEmpty mustEqual true
    }
  }
}
