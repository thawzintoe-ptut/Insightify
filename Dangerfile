github.dismiss_out_of_range_messages

warn("This PR is too big, please break it down into smaller chunks") if git.lines_of_code > 250

warn("PR is classed as Work in Progress, do not merge!") if github.pr_title.include? "[WIP]"

android_lint.report_file = "app/build/reports/lint-results-debug.xml"
android_lint.lint