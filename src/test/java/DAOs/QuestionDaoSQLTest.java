package DAOs;

import com.project.website.DAOs.*;
import com.project.website.Objects.Category;
import com.project.website.Objects.questions.AnswerableHTML;
import com.project.website.Objects.questions.QuestionEntry;
import com.project.website.Objects.questions.TextQuestion;
import com.project.website.utils.MySQLTestingTool;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionDaoSQLTest {

    private DataSource src;

    private UserDAO userDao;
    private CategoryDAO categoryDAO;

    private QuestionDAO questionDAO;
    private List<Integer> userIds;

    private List<Integer> categoryIds;

    private AnswerableHTML question;

    private List<Integer> generateQuestions() {
        List<Integer> questionIds = new ArrayList<>();
        questionIds.add(questionDAO.insertQuestion(new QuestionEntry(1, 1, question)));
        questionIds.add(questionDAO.insertQuestion(new QuestionEntry(2, 2, question)));
        questionIds.add(questionDAO.insertQuestion(new QuestionEntry(3, 3, question)));
        questionIds.add(questionDAO.insertQuestion(new QuestionEntry(4, 3, question)));
        return questionIds;
    }

    @Before
    public void setup() {
        src = MySQLTestingTool.getTestDataSource();
        try(Connection conn = src.getConnection()) {
            MySQLTestingTool.resetDB(conn, "sql/drop.sql", "sql/create.sql");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        userIds = new ArrayList<>();
        categoryIds = new ArrayList<>();

        userDao = new UserDAOSQL(src);
        categoryDAO = new CategoryDAOSQL(src);
        questionDAO = new QuestionDAOSQL(src);

        question = new TextQuestion("Who's Joe?", Collections.singletonList("Joe Mama"));

        userDao.register("user1", "examplePassword", "user1@gmail.com");
        userDao.register("user2", "examplePassword", "user2@gmail.com");
        userDao.register("user3", "examplePassword", "user3@gmail.com");
        userDao.register("user4", "examplePassword", "user4@gmail.com");

        categoryIds.add(categoryDAO.insertCategory(new Category("cat1")));
        categoryIds.add(categoryDAO.insertCategory(new Category("cat2")));
        categoryIds.add(categoryDAO.insertCategory(new Category("cat3")));
    }

    @Test
    public void testInsert() {
        Assertions.assertNotEquals(QuestionDAO.INSERT_FAILED, questionDAO.insertQuestion(new QuestionEntry(1, categoryIds.get(0), question)));
        Assertions.assertNotEquals(QuestionDAO.INSERT_FAILED, questionDAO.insertQuestion(new QuestionEntry(2, categoryIds.get(2), question)));
        Assertions.assertNotEquals(QuestionDAO.INSERT_FAILED, questionDAO.insertQuestion(new QuestionEntry(3, categoryIds.get(1), question)));
        Assertions.assertNotEquals(QuestionDAO.INSERT_FAILED, questionDAO.insertQuestion(new QuestionEntry(4, categoryIds.get(1), question)));

        Assertions.assertEquals(QuestionDAO.INSERT_FAILED, questionDAO.insertQuestion(new QuestionEntry(999, 1234123, question)));
        Assertions.assertEquals(QuestionDAO.INSERT_FAILED, questionDAO.insertQuestion(new QuestionEntry(1, 1234123, question)));
        Assertions.assertEquals(QuestionDAO.INSERT_FAILED, questionDAO.insertQuestion(new QuestionEntry(999, 2, question)));
        Assertions.assertEquals(QuestionDAO.INSERT_FAILED, questionDAO.insertQuestion(new QuestionEntry(1, 123, question)));
    }

    @Test
    public void testDelete() {
        List<Integer> questionIds = new ArrayList<>();
        questionIds.add(questionDAO.insertQuestion(new QuestionEntry(1, 1, question)));
        questionIds.add(questionDAO.insertQuestion(new QuestionEntry(2, 2, question)));
        questionIds.add(questionDAO.insertQuestion(new QuestionEntry(3, 3, question)));
        questionIds.add(questionDAO.insertQuestion(new QuestionEntry(4, 3, question)));

        Assertions.assertTrue(questionDAO.deleteQuestion(questionIds.get(0)));
        Assertions.assertFalse(questionDAO.deleteQuestion(questionIds.get(0)));

        Assertions.assertTrue(questionDAO.deleteQuestion(questionIds.get(3)));
        Assertions.assertFalse(questionDAO.deleteQuestion(questionIds.get(3)));

        Assertions.assertTrue(questionDAO.deleteQuestion(questionIds.get(1)));
        Assertions.assertFalse(questionDAO.deleteQuestion(questionIds.get(1)));

        Assertions.assertTrue(questionDAO.deleteQuestion(questionIds.get(2)));
        Assertions.assertFalse(questionDAO.deleteQuestion(questionIds.get(2)));

        Assertions.assertFalse(questionDAO.deleteQuestion(123));
        Assertions.assertFalse(questionDAO.deleteQuestion(111));
        Assertions.assertFalse(questionDAO.deleteQuestion(3333));
    }

    @Test
    public void testGetById() {
        List<QuestionEntry> questions = new ArrayList<>();
        questions.add(new QuestionEntry(1, 1, question));
        questions.add(new QuestionEntry(1, 2, question));
        questions.add(new QuestionEntry(3, 3, question));
        questions.add(new QuestionEntry(3, 3, question));

        List<Integer> questionIds = questions.stream().map(q -> questionDAO.insertQuestion(q)).collect(Collectors.toList());

        Assertions.assertNull(questionDAO.getQuestionById(123));
        Assertions.assertNull(questionDAO.getQuestionById(12321));
        Assertions.assertNull(questionDAO.getQuestionById(123121));

        QuestionEntry entry;
        entry = questionDAO.getQuestionById(1);
        Assertions.assertEquals(entry.getCreator_id(), questions.get(0).getCreator_id());
        Assertions.assertEquals(entry.getCategory_id(), questions.get(0).getCategory_id());
        Assertions.assertNotEquals(entry.getCreation_time(), QuestionEntry.NO_DATE);
        Assertions.assertEquals(entry.getQuestion().getAnswerParamNames(), question.getAnswerParamNames());
        Assertions.assertEquals(entry.getQuestion().getJSP(), question.getJSP());
    }

    @Test
    public void testGetByCreatorId() {
        List<Integer> questionIds = new ArrayList<>();
        questionIds.add(questionDAO.insertQuestion(new QuestionEntry(1, 1, question)));
        questionIds.add(questionDAO.insertQuestion(new QuestionEntry(1, 2, question)));
        questionIds.add(questionDAO.insertQuestion(new QuestionEntry(3, 3, question)));
        questionIds.add(questionDAO.insertQuestion(new QuestionEntry(3, 3, question)));

        List<QuestionEntry> questions = questionDAO.getQuestionsByCreatorId(1, 0, 111);
        Assertions.assertEquals(2, questions.size());
        Assertions.assertEquals(Arrays.asList(questionIds.get(0), questionIds.get(1)), questions.stream().map(QuestionEntry::getId).collect(Collectors.toList()));

        questions = questionDAO.getQuestionsByCreatorId(1, 0, 1);
        Assertions.assertEquals(1, questions.size());
        Assertions.assertEquals(Collections.singletonList(questionIds.get(0)), questions.stream().map(QuestionEntry::getId).collect(Collectors.toList()));

        questions = questionDAO.getQuestionsByCreatorId(1, 1, 1);
        Assertions.assertEquals(1, questions.size());
        Assertions.assertEquals(Collections.singletonList(questionIds.get(1)), questions.stream().map(QuestionEntry::getId).collect(Collectors.toList()));

        questions = questionDAO.getQuestionsByCreatorId(3, 0, 2);
        Assertions.assertEquals(2,questions.size());
        Assertions.assertEquals(Arrays.asList(questionIds.get(2), questionIds.get(3)), questions.stream().map(QuestionEntry::getId).collect(Collectors.toList()));

        questions = questionDAO.getQuestionsByCreatorId(3, 0, 1);
        Assertions.assertEquals(1, questions.size());
        Assertions.assertEquals(Collections.singletonList(questionIds.get(2)), questions.stream().map(QuestionEntry::getId).collect(Collectors.toList()));

        questions = questionDAO.getQuestionsByCreatorId(3, 1, 1);
        Assertions.assertEquals(1, questions.size());
        Assertions.assertEquals(Collections.singletonList(questionIds.get(3)), questions.stream().map(QuestionEntry::getId).collect(Collectors.toList()));

        questions = questionDAO.getQuestionsByCreatorId(23, 1, 1);
        Assertions.assertEquals(0, questions.size());

        questions = questionDAO.getQuestionsByCreatorId(444, 1, 1);
        Assertions.assertEquals(0, questions.size());
    }

    @Test
    public void testGetByCategoryId() {
        List<Integer> questionIds = new ArrayList<>();
        questionIds.add(questionDAO.insertQuestion(new QuestionEntry(1, 1, question)));
        questionIds.add(questionDAO.insertQuestion(new QuestionEntry(2, 1, question)));
        questionIds.add(questionDAO.insertQuestion(new QuestionEntry(3, 3, question)));
        questionIds.add(questionDAO.insertQuestion(new QuestionEntry(4, 3, question)));

        List<QuestionEntry> questions = questionDAO.getQuestionsByCategory(1, 0, 111);
        Assertions.assertEquals(2, questions.size());
        Assertions.assertEquals(Arrays.asList(questionIds.get(0), questionIds.get(1)), questions.stream().map(QuestionEntry::getId).collect(Collectors.toList()));

        questions = questionDAO.getQuestionsByCategory(1, 0, 1);
        Assertions.assertEquals(1, questions.size());
        Assertions.assertEquals(Collections.singletonList(questionIds.get(0)), questions.stream().map(QuestionEntry::getId).collect(Collectors.toList()));

        questions = questionDAO.getQuestionsByCategory(1, 1, 1);
        Assertions.assertEquals(1, questions.size());
        Assertions.assertEquals(Collections.singletonList(questionIds.get(1)), questions.stream().map(QuestionEntry::getId).collect(Collectors.toList()));

        questions = questionDAO.getQuestionsByCategory(3, 0, 2);
        Assertions.assertEquals(2,questions.size());
        Assertions.assertEquals(Arrays.asList(questionIds.get(2), questionIds.get(3)), questions.stream().map(QuestionEntry::getId).collect(Collectors.toList()));

        questions = questionDAO.getQuestionsByCategory(3, 0, 1);
        Assertions.assertEquals(1, questions.size());
        Assertions.assertEquals(Collections.singletonList(questionIds.get(2)), questions.stream().map(QuestionEntry::getId).collect(Collectors.toList()));

        questions = questionDAO.getQuestionsByCategory(3, 1, 1);
        Assertions.assertEquals(1, questions.size());
        Assertions.assertEquals(Collections.singletonList(questionIds.get(3)), questions.stream().map(QuestionEntry::getId).collect(Collectors.toList()));

        questions = questionDAO.getQuestionsByCategory(23, 1, 1);
        Assertions.assertEquals(0, questions.size());

        questions = questionDAO.getQuestionsByCategory(444, 1, 1);
        Assertions.assertEquals(0, questions.size());
    }
}
