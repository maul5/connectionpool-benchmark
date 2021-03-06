package connectionpools.impl

import java.util
import javax.sql.DataSource

import connectionpools.DataSources._
import connectionpools.{DataSourceFactorySupport, JdbcDrivers}
import org.apache.tomcat.jdbc.pool
import org.apache.tomcat.jdbc.pool.PoolProperties

/**
 * TomcatCPDataSourceFactory
 * @author Sunghyouk Bae sunghyouk.bae@gmail.com
 */
class TomcatCPDataSourceFactory extends DataSourceFactorySupport {

  /**
   * Tomcat DataSource를 생성합니다.
   * @param dataSourceClassName dataSourceClassName
   *                            ( 기존 driverClass 가 아닙니다 : mysql용은 com.mysql.jdbc.jdbc2.optional.MysqlDataSource 입니다 )
   * @param url         Database 주소
   * @param username    사용자 명
   * @param passwd      사용자 패스워드
   * @return [[javax.sql.DataSource]] 인스턴스
   */
  override def createDataSource(dataSourceClassName: String = "",
                                driverClass: String = JdbcDrivers.DRIVER_CLASS_H2,
                                url: String = "jdbc:h2:mem:test",
                                username: String = "",
                                passwd: String = "",
                                props: util.Map[String, String] = new util.HashMap(),
                                maxPoolSize: Int = MAX_POOL_SIZE): DataSource = {
    log.info("Tomcat DataSource를 빌드합니다... " +
             s"dataSourceClassName=[$dataSourceClassName], driverClass=[$driverClass] url=[$url], username=[$username]")

    val p = new PoolProperties()

    p.setDriverClassName(driverClass)
    p.setUrl(url)
    p.setUsername(username)
    p.setPassword(passwd)

    p.setInitialSize(MIN_POOL_SIZE)
    p.setMaxActive(MAX_POOL_SIZE)
    p.setMaxIdle(MIN_POOL_SIZE)
    p.setMinIdle(MIN_IDLE_SIZE)

    p.setTimeBetweenEvictionRunsMillis(30000)
    p.setMinEvictableIdleTimeMillis(30000)
    p.setValidationQuery("SELECT 1")
    p.setValidationInterval(34000)
    p.setTestOnBorrow(true)
    p.setMaxWait(10000)
    p.setLogAbandoned(true)
    p.setRemoveAbandoned(true)

    log.info(s"Tomcat DataSource 설정 정보=$p")

    new pool.DataSource(p)
  }
}
