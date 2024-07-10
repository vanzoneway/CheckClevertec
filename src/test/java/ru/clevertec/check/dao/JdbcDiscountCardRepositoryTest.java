package ru.clevertec.check.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.exception.NoSuchProductIdException;
import ru.clevertec.check.exception.ProductNotFoundException;
import ru.clevertec.check.model.DiscountCard;

import java.sql.*;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JdbcDiscountCardRepositoryTest {
    @Mock
    private DatabaseConnection databaseConnectionMock;
    @Mock
    private Connection connectionMock;
    @Mock
    private Statement statementMock;
    @Mock
    private PreparedStatement preparedStatementMock;
    @Mock
    private ResultSet resultSetMock;

    @InjectMocks
    private JdbcDiscountCardRepository repository;



    @Test
    void getDiscountCardByNumber_ReturnsExpectedDiscountCard() throws SQLException {

        int number = 12345;
        when(databaseConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.createStatement()).thenReturn(statementMock);
        when(statementMock.executeQuery("SELECT * FROM discount_card WHERE number = 12345")).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt("number")).thenReturn(number);
        when(resultSetMock.getInt("amount")).thenReturn(100);
        when(resultSetMock.getInt("id")).thenReturn(1);

        DiscountCard discountCard = repository.getDiscountCardByNumber(number);

        assertEquals(number, discountCard.getNumber());
        assertEquals(100, discountCard.getAmount());
        assertEquals(1, discountCard.getId());
    }

    @Test
    public void testGetDiscountCardById() throws SQLException, ClassNotFoundException, NoSuchProductIdException {
        // Arrange
        int discountCardId = 123;
        DiscountCard expectedDiscountCard = new DiscountCard();
        expectedDiscountCard.setId(discountCardId);
        expectedDiscountCard.setNumber(456);
        expectedDiscountCard.setAmount(100);

        when(databaseConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.createStatement()).thenReturn(statementMock);
        when(connectionMock.createStatement()).thenReturn(statementMock);
        when(statementMock.executeQuery(anyString())).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt("id")).thenReturn(expectedDiscountCard.getId());
        when(resultSetMock.getInt("number")).thenReturn(expectedDiscountCard.getNumber());
        when(resultSetMock.getInt("amount")).thenReturn(expectedDiscountCard.getAmount());

        DiscountCard actualDiscountCard = repository.getDiscountCardById(discountCardId);

        assertEquals(expectedDiscountCard.getId(), actualDiscountCard.getId());
        verify(statementMock).close();
    }

    @Test
    public void testUpdateDiscountCard() throws Exception {

        DiscountCard discountCard = new DiscountCard();
        discountCard.setId(123);
        discountCard.setNumber(456);
        discountCard.setAmount(100);


        when(databaseConnectionMock.getConnection()).thenReturn(connectionMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        repository.updateDiscountCard(discountCard);

        verify(preparedStatementMock).setInt(1, discountCard.getNumber());
        verify(preparedStatementMock).setInt(2, discountCard.getAmount());
        verify(preparedStatementMock).setInt(3, discountCard.getId());
        verify(preparedStatementMock).executeUpdate();
        verify(preparedStatementMock).close();
    }

    @Test
    public void testUpdateDiscountCardWithNoRowsAffected() throws Exception {

        DiscountCard discountCard = new DiscountCard();
        discountCard.setId(123);
        discountCard.setNumber(456);
        discountCard.setAmount(100);

        when(databaseConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(0);


        assertThrows(NoSuchProductIdException.class, () -> repository.updateDiscountCard(discountCard));
    }

    @Test
    public void testDeleteDiscountCardById() throws Exception {
        // Arrange
        int discountCardId = 123;

        when(databaseConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        // Act
        repository.deleteDiscountCardById(discountCardId);

        // Assert
        verify(preparedStatementMock).setInt(1, discountCardId);
        verify(preparedStatementMock).executeUpdate();
        verify(preparedStatementMock).close();
    }

    @Test
    public void testDeleteDiscountCardByIdWithNoRowsAffected() throws Exception {
        // Arrange
        int discountCardId = 123;

        when(databaseConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(0);

        // Act and Assert
        assertThrows(ProductNotFoundException.class, () -> repository.deleteDiscountCardById(discountCardId));
    }

    @Test
    public void testDeleteDiscountCardByIdWithSqlException() throws Exception {
        // Arrange
        int discountCardId = 123;

        when(databaseConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenThrow(new SQLException("Database error"));

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> repository.deleteDiscountCardById(discountCardId));
        assert exception.getMessage().startsWith("Error deleting discount card:");
    }




    @Test
    public void testCreateDiscountCard() throws Exception {
        // Arrange
        DiscountCard discountCard = new DiscountCard();
        discountCard.setNumber(123);
        discountCard.setAmount(50);

        when(databaseConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString(), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);
        when(preparedStatementMock.getGeneratedKeys()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt(1)).thenReturn(456);

        // Act
        repository.createDiscountCard(discountCard);

        // Assert
        assertEquals(456, discountCard.getId());
        verify(preparedStatementMock).setInt(1, 123);
        verify(preparedStatementMock).setInt(2, 50);
        verify(preparedStatementMock).executeUpdate();
        verify(preparedStatementMock).getGeneratedKeys();
        verify(resultSetMock).next();
        verify(resultSetMock).getInt(1);
        verify(preparedStatementMock).close();
    }

}