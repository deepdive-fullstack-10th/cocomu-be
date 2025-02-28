## 협업 강령

> COCOMU Project의 협업 강령입니다.

---

### Branch Strategy

- GitHub Flow 전략으로 작업을 진행한다.
- branch name은 jiraIssueNumber-type/#issueNumber 로 작성한다.
  - coc-101-feat/#1
  - type은 feat, refactor, fix만 존재한다.

### Commit Convention

- 기본적으로 js angular commit convention을 따른다.
- type(#issueNumber): commit title
  - type은 feat, refactor, fix, chore 만 존재한다.
- 기존 코드에 영향이 가는 부분이 있다면 커밋 본문에 필시 작성한다.

### Code Style

1. 변수에 final keyword를 꼭 작성한다.
2. Class 블럭 내 첫줄과 마지막 줄에는 개행을 작성한다.
3. 메서드 내 파라미터에 개행이 삽입된다면, indent는 4이며 모든 파라미터를 개행처리한다.
4. 테스트는 BDD(given, when, then)를 따른다.
5. 테스트 메서드 명은 한글로 작성한다.