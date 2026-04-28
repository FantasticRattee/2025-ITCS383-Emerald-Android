# D2: Quality Evidence Report

**By:** Team Emerald
**Date:** 2026-04-28
**Original Team:** Arai-Kor-Dai (6688046, 6688194, 6688083, 6688148, 6688205, 6688226)

### Team Emerald Members

| Student ID | Name |
|------------|------|
| 6688139 | Naruebordint Veangnont |
| 6688141 | Rattee Watperatam |
| 6688155 | Nattaphong Jullayakiat |
| 6688164 | Veerakron No-in |
| 6688172 | Wirunya Kaewthong |
| 6688239 | Piyada Chalermnontakarn |

---

## 1. Introduction

This report presents a comprehensive quality evidence analysis for the **PostOfficeApp Android** project, conducted using **SonarCloud** as the primary static code analysis tool. SonarCloud provides automated inspection of code quality by detecting bugs, vulnerabilities, code smells, duplicated code, and measuring test coverage against industry-standard quality gates.

The objective of this report is to demonstrate measurable improvement in software quality between the previous code state and the current state, supported by concrete metrics extracted from SonarCloud analysis results. All values reported herein are derived exclusively from the original D4 report and the latest SonarCloud dashboard screenshots.

**SonarCloud Project Link:** https://sonarcloud.io/component_measures?metric=new_bugs&id=peemnoin_2025-ITCS383-Emerald-Android

---

## 2. Old Code Analysis

The previous quality report indicated that the project passed the SonarCloud Quality Gate with the following recorded metrics:

- **Quality Gate Status:** Passed
- **Blocker Issues:** 0
- **High Severity Issues:** 0
- **Bugs:** Not Available (not explicitly reported)
- **Vulnerabilities:** Not Available (not explicitly reported)
- **Code Smells:** Not Available (not explicitly reported)
- **Coverage:** Not Available (not tracked in the previous analysis)
- **Duplications:** Not Available (not tracked in the previous analysis)

### Limitations of the Old Analysis

The previous report focused primarily on confirming a passing quality gate and the absence of blocker/high-severity issues. However, several critical quality dimensions were not tracked or reported:

1. **No coverage data** — Unit test coverage was not measured, making it impossible to assess how well the codebase was tested.
2. **No duplication metrics** — Code duplication was not monitored, leaving potential maintainability risks undetected.
3. **Incomplete severity breakdown** — Only blocker and high-severity categories were reported; medium, low, and informational issues were omitted.
4. **No detailed issue categorization** — The report did not distinguish between bugs, vulnerabilities, and code smells, limiting the depth of quality assessment.

---

## 3. Improvements Made

Between the old and new analyses, the following technical improvements were implemented:

### 3.1 Unit Test Implementation

Comprehensive unit tests were added to the project, achieving **95.1% code coverage** across 262 lines to cover. This represents a significant investment in test quality, as the previous iteration had no recorded coverage data. The coverage increase of +10.7% over the last 30 days (visible in the SonarCloud dashboard) indicates ongoing and active test development.

### 3.2 Code Duplication Elimination

The codebase achieved **0.0% code duplication** across 477 lines analyzed. This indicates that duplicated logic was either refactored into shared utility methods or was avoided from the outset through disciplined software design practices.

### 3.3 Issue Resolution and Prevention

All issue categories — bugs, vulnerabilities, and code smells — were reduced to zero across every severity level (Blocker, Critical, Major, Minor, Info). This was achieved through:

- Adherence to coding standards enforced by SonarCloud rules
- Code review practices that catch issues before merging
- Refactoring efforts targeting maintainability and readability

### 3.4 Security Hardening

The project achieved an **A rating** for both Security and Security Review categories, with zero security issues and zero security hotspots. This confirms that the codebase follows secure coding practices.


### 3.5 Issue Resolution Evidence

The SonarCloud issue tracking interface provides concrete evidence of code quality improvements through the resolution of previously identified maintainability issues.

A total of **17 code smell issues** were identified in the earlier analysis and have now been fully resolved. These issues were distributed across multiple severity levels:

- **Critical:** 8 issues  
- **Major:** 5 issues  
- **Minor:** 4 issues  

All identified issues are currently marked with the status **“Fixed”**, confirming that no outstanding code smell issues remain in the codebase.

### Key Refactoring Actions

The resolution of these issues involved several targeted refactoring efforts:

- **Reduction of Cognitive Complexity**  
  A complex method in `CreateShipmentScreen.kt` was refactored to reduce its cognitive complexity from **35 to the allowed threshold of 15**, improving code readability and maintainability.

- **Removal of Unused Variables**  
  Unused local variables such as `userId`, `isLoading`, and `scope` were removed. This reduces unnecessary code clutter and improves overall code clarity.

- **Elimination of Duplicate Literals**  
  Repeated literal values (e.g., formatting strings) were replaced with constants, ensuring better consistency and maintainability across the codebase.

### Impact on Maintainability

These improvements directly contribute to enhanced maintainability by:

- Simplifying complex logic structures  
- Reducing redundant and unused code  
- Improving adherence to coding standards enforced by SonarCloud  

As a result of these changes, the project now achieves **zero code smells across all severity levels**, aligning with the highest maintainability standards.

<img width="1681" height="895" alt="image" src="https://github.com/user-attachments/assets/a3098b08-e09c-4c92-a6fb-16736819bea6" />


---

## 4. New Code Analysis

The latest SonarCloud analysis of the **PostOfficeApp Android** project (365 Lines of Code, 19 files) shows the following results:

### Quality Gate

- **Status:** Passed — All conditions passed

### Software Quality Ratings

| Category        | Rating | Open Issues |
|-----------------|--------|-------------|
| Security        | A      | 0           |
| Reliability     | A      | 0           |
| Maintainability | A      | 0           |

<img width="1920" height="853" alt="image" src="https://github.com/user-attachments/assets/6f69f39d-e650-4db3-b4c2-87256c9d7c27" />


### Issue Breakdown by Severity

| Severity | Count |
|----------|-------|
| Blocker  | 0     |
| High     | 0     |
| Medium   | 0     |
| Low      | 0     |
| Info     | 0     |

### Issue Breakdown by Type

| Type          | Count |
|---------------|-------|
| Bug           | 0     |
| Vulnerability | 0     |
| Code Smell    | 0     |

### Code Attribute Analysis

| Attribute      | Issues |
|----------------|--------|
| Consistency    | 0      |
| Intentionality | 0      |
| Adaptability   | 0      |
| Responsibility | 0      |

<img width="1918" height="877" alt="image" src="https://github.com/user-attachments/assets/72f7a299-5518-4c68-a183-6228eb94642d" />


### Coverage and Duplication

| Metric                | Value  | Details                    |
|-----------------------|--------|----------------------------|
| Coverage              | 95.1%  | 262 lines to cover         |
| Duplicated Lines      | 0.0%   | 477 lines analyzed         |
| Security Hotspots     | 0      | -100.0% change (last 30 days) |
| Accepted Issues       | 0      | No valid issues left unfixed |

### Security Snapshot

- **Security Rating:** A (Overall Code)
- **Security Issues:** 0 (no change in last 30 days)
- **Security Review Rating:** A (Overall Code)
- **Security Hotspots:** 0

### Reliability Snapshot

- **Reliability Rating:** A (New Code and Overall Code)
- **Vulnerabilities:** 0 (New Code and Overall Code)
- **Bugs:** 0
- **Remediation Effort:** 0

---

## 5. Comparison Table

| Metric              | Old Code         | New Code         | Change           |
|----------------------|------------------|------------------|------------------|
| Quality Gate         | Passed           | Passed           | Maintained       |
| Bugs                 | Not Available    | 0                | —                |
| Vulnerabilities      | Not Available    | 0                | —                |
| Code Smells          | Not Available    | 0                | —                |
| Blocker Issues       | 0                | 0                | No change        |
| High Severity Issues | 0                | 0                | No change        |
| Coverage             | Not Available    | 95.1%            | Newly tracked    |
| Duplicated Lines     | Not Available    | 0.0%             | Newly tracked    |
| Security Rating      | Not Available    | A                | Newly tracked    |
| Reliability Rating   | Not Available    | A                | Newly tracked    |
| Maintainability Rating | Not Available  | A                | Newly tracked    |
| Security Hotspots    | Not Available    | 0                | Newly tracked    |
| Lines of Code        | Not Available    | 365              | Newly tracked    |
| Files Analyzed       | Not Available    | 19               | Newly tracked    |

### Key Observations

1. **Coverage visibility** is the most significant improvement — going from untracked to 95.1% demonstrates a mature testing practice.
2. **Zero issues across all categories** confirms that the codebase is clean and well-maintained.
3. **Full metric tracking** in the new analysis provides a far more complete and actionable quality picture compared to the limited old report.

---

## 6. Continuous Integration

The project integrates **GitHub Actions** to automate quality checks as part of the CI/CD pipeline. This setup ensures that:

1. **Automated analysis on every push/PR** — SonarCloud analysis runs automatically through GitHub Actions, preventing unreviewed code from entering the main branch.
2. **Quality gate enforcement** — The CI pipeline checks that the SonarCloud quality gate passes before code can be merged, acting as an automated gatekeeper.
3. **Continuous feedback loop** — Developers receive immediate feedback on code quality issues, enabling faster resolution and preventing technical debt accumulation.
4. **Reproducible builds** — The automated pipeline ensures that quality checks are consistent and not dependent on individual developer environments.

This integration is critical for maintaining the zero-issue status observed in the current analysis, as it shifts quality assurance from a manual, post-hoc activity to an automated, continuous process.

---

## 7. Conclusion

The SonarCloud analysis demonstrates that the **PostOfficeApp Android** project has achieved a high standard of software quality:

- **All quality ratings are A** across security, reliability, and maintainability.
- **Zero issues** exist at every severity level and across all issue types (bugs, vulnerabilities, code smells).
- **95.1% test coverage** ensures that the vast majority of the codebase is verified by automated tests.
- **0.0% code duplication** reflects disciplined code design and effective refactoring.

Compared to the previous report, the most significant improvement is the introduction of comprehensive metric tracking — particularly coverage and duplication — which provides a complete and transparent view of code health. The integration of GitHub Actions ensures that these quality standards are maintained continuously as the project evolves.

The project meets and exceeds the required code quality standards for the D4 deliverable.
