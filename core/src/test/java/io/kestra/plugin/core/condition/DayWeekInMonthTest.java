package io.kestra.plugin.core.condition;

import com.google.common.collect.ImmutableMap;
import io.kestra.core.models.executions.Execution;
import io.kestra.core.models.flows.Flow;
import io.kestra.core.services.ConditionService;
import io.kestra.core.utils.TestsUtils;
import io.kestra.core.junit.annotations.KestraTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.stream.Stream;
import jakarta.inject.Inject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@KestraTest
class DayWeekInMonthTest {
    @Inject
    ConditionService conditionService;

    static Stream<Arguments> source() {
        return Stream.of(
            Arguments.of(LocalDate.parse("2021-04-01").toString(), DayOfWeek.MONDAY, DayWeekInMonth.DayInMonth.FIRST, false),
            Arguments.of(LocalDate.parse("2021-04-05").toString(), DayOfWeek.MONDAY, DayWeekInMonth.DayInMonth.FIRST, true),
            Arguments.of(LocalDate.parse("2021-04-01").toString(), DayOfWeek.THURSDAY, DayWeekInMonth.DayInMonth.FIRST, true),
            Arguments.of(LocalDate.parse("2021-04-01").toString(), DayOfWeek.MONDAY, DayWeekInMonth.DayInMonth.LAST, false),
            Arguments.of(LocalDate.parse("2021-04-26").toString(), DayOfWeek.MONDAY, DayWeekInMonth.DayInMonth.LAST, true),
            Arguments.of(LocalDate.parse("2021-04-12").toString(), DayOfWeek.MONDAY, DayWeekInMonth.DayInMonth.SECOND, true),
            Arguments.of(LocalDate.parse("2021-04-19").toString(), DayOfWeek.MONDAY, DayWeekInMonth.DayInMonth.THIRD, true),
            Arguments.of(LocalDate.parse("2021-04-26").toString(), DayOfWeek.MONDAY, DayWeekInMonth.DayInMonth.FOURTH, true)
        );
    }

    @ParameterizedTest
    @MethodSource("source")
    void valid(String date, DayOfWeek dayOfWeek, DayWeekInMonth.DayInMonth dayInMonth, boolean result) {
        Flow flow = TestsUtils.mockFlow();
        Execution execution = TestsUtils.mockExecution(flow, ImmutableMap.of());

        DayWeekInMonth build = DayWeekInMonth.builder()
            .date(date)
            .dayOfWeek(dayOfWeek)
            .dayInMonth(dayInMonth)
            .build();

        boolean test = conditionService.isValid(build, flow, execution);

        assertThat(test, is(result));
    }
}