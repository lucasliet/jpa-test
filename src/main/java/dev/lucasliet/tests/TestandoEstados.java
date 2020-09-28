package dev.lucasliet.tests;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import dev.lucasliet.model.Conta;

public class TestandoEstados {
	public static void main(String[] args) {
		Conta conta = new Conta();
		conta.setTitular("Almiro");
		conta.setAgencia(123);
		conta.setNumero(321);
		
		EntityManager em = Persistence.createEntityManagerFactory("contas").createEntityManager();
		
		// Objeto em estado Managed
		// Todas as mudan�as devem estar em uma transa��o, caso haja algum problema,
		// Ser� feito o Rollback
		em.getTransaction().begin();
			conta.setSaldo(400.00);
			em.persist(conta);
			em.getTransaction().commit();
		em.close();

		conta.setAgencia(1234);

		// Objetos em estado Detached
		// Mudan�as feitas fora da transa��o devem ser commitadas novamente com o merge
		// Para realizar um update no banco de dados (o id ainda permanesce o mesmo)

		EntityManager em2 = Persistence.createEntityManagerFactory("contas").createEntityManager();

		em2.getTransaction().begin();
			em2.merge(conta);
		em2.getTransaction().commit();

		em2.getTransaction().begin();
			// Objeto em estado Removed
			// O Objeto ainda possui um ID, mas foi removido e n�o est� mais sendo
			// gerenciado
			// no banco
			em2.remove(conta);
		em2.getTransaction().commit();
		em2.close();
	}

}
