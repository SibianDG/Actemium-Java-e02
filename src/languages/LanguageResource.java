package languages;

	import java.util.Locale;
	import java.util.ResourceBundle;

	public class LanguageResource {

		private static Locale locale;
		private static ResourceBundle bundle;
		private static final String location = "languages/resourcebundle/languages";

		static {
			setLocale(new Locale("en"));
			setBundle();
		}

		public LanguageResource() {
			setLocale(new Locale("en"));
			setBundle();
		}

		public LanguageResource(Locale locale) {
			setLocale(locale);
		}

		public static Locale getLocale() {
			return LanguageResource.locale;
		}

		public static void setLocale(Locale locale) {
			if (locale.toString().equals("en") || locale.toString().equals("fr") || locale.toString().equals("nl") || locale.toString().equals("de")) {
				LanguageResource.locale = locale;
			} else {
				LanguageResource.locale = new Locale("en");
			}
			setBundle();
		}

		public ResourceBundle getBundle() {
			return bundle;
		}

		private static void setBundle() {
			LanguageResource.bundle = ResourceBundle.getBundle(location, getLocale());
		}

		public static String getString(String string) {
			return bundle.getString(string);
		}

	}


