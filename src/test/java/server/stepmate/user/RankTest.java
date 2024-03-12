package server.stepmate.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static server.stepmate.user.RankTest.User.calculateRankings;

public class RankTest {
    public class User{
        private String name;
        private int score;
        private int level;
        private int rank;

        public User(String name, int score, int level) {
            this.name = name;
            this.score = score;
            this.level = level;
        }
        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }

        public int getLevel() {
            return level;
        }
        public int getRank() {
            return rank;
        }

        public static Comparator<User> userComparator = Comparator
                .comparingInt(User::getScore)     // 점수 오름차순
                .reversed()
                .thenComparingInt(User::getLevel)  // 레벨 오름차순
                .thenComparing(User::getName, Comparator.reverseOrder());  // 이름 내림차순

        // 순위 계산
        public static List<User> calculateRankings(List<User> userList) {
            userList.sort(userComparator);

            int rank = 1;
            int currentRank = 1;
            int previousScore = Integer.MAX_VALUE;

            for (User user : userList) {
                if (user.getScore() == previousScore) {
                    user.setRank(currentRank); // 이전 유저와 점수, 레벨이 같으면 이전 유저의 순위와 동일
                } else {
                    user.setRank(rank);
                    currentRank = rank;
                }

                previousScore = user.getScore();
                rank++;
            }

            return userList;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }
    }

    @Test
    @DisplayName("랭킹 선정 테스트")
    void test() {
        List<User> userList = new ArrayList<>();
        userList.add(new User("John", 100, 3));
        userList.add(new User("Alice", 90, 2));
        userList.add(new User("Bob", 90, 1));
        userList.add(new User("HA", 90, 1));
        userList.add(new User("Charlie", 80, 3));

        // 순위 계산
        List<User> rankedUsers = calculateRankings(userList);

        // 출력
        for (User user : rankedUsers) {
            System.out.println("Name: " + user.getName() +
                    ", Score: " + user.getScore() +
                    ", Level: " + user.getLevel() +
                    ", Rank: " + user.getRank());
        }
    }
}
