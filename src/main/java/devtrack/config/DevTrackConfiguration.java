package devtrack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import devtrack.mapper.IssueMapper;
import devtrack.model.Issue;
import devtrack.repository.InMemoryRepository;
import devtrack.service.IssueService;

@Configuration
public class DevTrackConfiguration {

    @Bean
    public InMemoryRepository<Issue, String> createIssueRepository() {

        return new InMemoryRepository<>();
    }

    @Bean
    public IssueService createIssueService(InMemoryRepository<Issue, String> issueRepository) {

        return new IssueService(issueRepository);
    }

    @Bean
    public IssueMapper createIssueMapper() {

        return new IssueMapper();
    }

}
