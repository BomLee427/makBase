package io.bom.makBase.config

import io.bom.makBase.service.RegionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationRunnerConfig {
    @Bean
    fun applicationRunner(): ApplicationRunner {
        return object : ApplicationRunner {
            @Autowired
            lateinit var regionService: RegionService

            @Throws(Exception::class)
            override fun run(args: ApplicationArguments) {
                regionService.cacheRegion()
            }
        }
    }
}
