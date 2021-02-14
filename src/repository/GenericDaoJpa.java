package repository;

public class GenericDaoJpa {

	private static String PU_NAME;
	private static EnitityManagerFactory emf;
	private static EnitityManager em;
	private Class<T> type;

	public GenericDaoJpa(Class<T> type) {
		throw new UnsupportedOperationException();
	}

	public static void closePersistency() {
		throw new UnsupportedOperationException();
	}

	public static void startTransaction() {
		throw new UnsupportedOperationException();
	}

	public static void commitTransaction() {
		throw new UnsupportedOperationException();
	}

	public static void rollbackTransaction() {
		throw new UnsupportedOperationException();
	}
}
