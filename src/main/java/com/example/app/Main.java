package com.example.app;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Random;

public class Main {
    private static final SessionFactory ourSessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            ourSessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    public static void main(final String[] args) throws Exception {
        try (Session session = getSession()) {
            Transaction transaction = session.beginTransaction();

            User user = session.load(User.class, 1);
            System.out.println(user);

            // user.getMessages().forEach(System.out::println);

            // inserting message
            /* before:
            id  from  subject    sent_at           content
            1   1     testing    2019-03-10 11:20  hello
            2   2     testing    2019-03-10 11:20  oh, hi!
            3   1     testing    2019-03-10 11:26  my second message!
            */
            composeMessage(session);
            /* after:
            +++
            4   1     Hibernate  2019-03-10 14:44  com.example.app.Message from Hibernate! (can be repeated)
             */

            // renaming
            /* before:
            id  email      passwd   name
            1   a@mail.ru  qewrtyu  Dmitry
             */
            renameUser(session);
            /* after:
            1   a@mail.ru  qewrtyu  My name is 646413777
             */

            // creating user
            User newUser = newUser();
            session.save(newUser);

            // deleting user
            // session.delete(newUser());

            transaction.commit();
        }
    }

    private static User newUser() {
        User newUser = new User();
        newUser.setName("Olegsander");
        newUser.setEmail("oles@mail.com");
        newUser.setPassword("9876543");
        return newUser;
    }

    private static void renameUser(Session session) {
        User user = session.get(User.class, 1);
        user.setName("My name is " + new Random().nextInt());
        session.save(user);
    }

    private static void composeMessage(Session session) {
        User user = session.load(User.class, 1);
        Message message = new Message();
//            message.setFrom(user);
        message.setContent("com.example.app.Message from Hibernate! (can be repeated) " + new Random().nextInt());
        message.setRecipients(
                new LinkedList<User>() {{ add(session.load(User.class, 2)); }}
        );
        message.setSentAt(LocalDateTime.now());
        message.setSubject("Hibernate");

        session.save(message);

        user.getMessages().add(message);

        session.save(user);
    }
}