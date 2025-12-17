package scheduler

import io.github.flaxoos.ktor.server.plugins.taskscheduling.managers.TaskManagerConfiguration.TaskManagerName.Companion.toTaskManagerName
import io.github.flaxoos.ktor.server.plugins.taskscheduling.managers.lock.database.DatabaseTaskLockManagerConfiguration
import io.github.flaxoos.ktor.server.plugins.taskscheduling.managers.lock.database.ExposedTaskLockTable
import io.github.flaxoos.ktor.server.plugins.taskscheduling.managers.lock.database.JdbcLockManager
import io.ktor.server.application.*
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp


/**
 * TaskScheduling 플러그인의 JDBC 락 테이블 정의
 *
 * ## 중요: 직접 수정 금지 ⚠️
 *
 * Exposed와 TaskScheduling 라이브러리 버전 불일치로 인한 호환성 문제를 해결하기 위해 직접 구현되었습니다.
 *
 * ### 배경
 * - MariaDB/MySQL은 TEXT 타입을 PRIMARY KEY로 사용할 수 없음
 * - 기본 TaskScheduling 구현은 TEXT를 사용하여 에러 발생
 * - VARCHAR(255)로 오버라이드하여 해결
 *
 * ### 수정 시기
 * - Exposed 또는 TaskScheduling 라이브러리 메이저 버전 업데이트 시
 * - 공식 라이브러리에서 MariaDB/MySQL 지원이 개선된 경우
 *
 * @see ExposedTaskLockTable
 */
private object TaskLockTable : ExposedTaskLockTable("task_locks") {
    /** 태스크 이름 (VARCHAR로 변경하여 PRIMARY KEY 호환성 확보) */
    override val name: Column<String> = varchar("_name", 255)

    /** 동시 실행 인덱스 */
    override val concurrencyIndex: Column<Int> = integer("concurrency_index")

    /** 락 획득 시간 */
    override val lockedAt: Column<Instant?> = timestamp("locked_at").nullable().index()

    /** 복합 PRIMARY KEY (name + concurrencyIndex) */
    override val primaryKey: PrimaryKey = PrimaryKey(firstColumn = name, concurrencyIndex, name = "pk_task_locks")
}

/**
 * 커스텀 JDBC 락 매니저 설정
 *
 * ## 중요: 직접 수정 금지 ⚠️
 *
 * Exposed와 TaskScheduling 라이브러리 버전 불일치로 인한 호환성 문제를 해결하기 위해 직접 구현되었습니다.
 *
 * ### 역할
 * - 위에 정의된 [TaskLockTable]을 사용하는 JdbcLockManager 생성
 * - 분산 환경에서 태스크 중복 실행 방지
 *
 * @param database Exposed Database 인스턴스
 * @see JdbcLockManager
 * @see TaskLockTable
 */
internal class DefaultJdbcJobLockManagerConfiguration(
    private val database: Database
) : DatabaseTaskLockManagerConfiguration() {

    override fun createTaskManager(application: Application): JdbcLockManager =
        JdbcLockManager(
            name = name.toTaskManagerName(),
            application = application,
            database = database,
            taskLockTable = TaskLockTable,
        )
}