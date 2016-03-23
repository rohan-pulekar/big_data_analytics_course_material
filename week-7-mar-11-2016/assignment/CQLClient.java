package edu.hu.cassandra;

	import com.datastax.driver.core.Cluster;
	import com.datastax.driver.core.Host;
	import com.datastax.driver.core.Metadata;
	import com.datastax.driver.core.Session;
	import com.datastax.driver.core.ResultSet;
	import com.datastax.driver.core.Row;
	
	public class CQLClient {
	   private Cluster cluster;
	   private Session session;

	   public void connect(String node) {
	      cluster = Cluster.builder()
	            .addContactPoint(node).build();
	      session = cluster.connect("mykeyspace");
	      Metadata metadata = cluster.getMetadata();
	      System.out.printf("Connected to cluster: %s\n", 
	            metadata.getClusterName());
	      for ( Host host : metadata.getAllHosts() ) {
	         System.out.printf("Datatacenter: %s; Host: %s; Rack: %s\n",
	               host.getDatacenter(), host.getAddress(), host.getRack());
	      }
	   }

	   public void createSchema() {
//		   session.execute("CREATE KEYSPACE mykeyspace WITH replication " + 
//				      "= {'class':'SimpleStrategy', 'replication_factor':1};");
		   session.execute(
				      "CREATE TABLE mykeyspace.songs (" +
				            "id uuid PRIMARY KEY," + 
				            "title text," + 
				            "album text," + 
				            "artist text," + 
				            "tags set<text>," + 
				            "data blob" + 
				            ");");
				session.execute(
				      "CREATE TABLE mykeyspace.playlists (" +
				            "id uuid," +
				            "title text," +
				            "album text, " + 
				            "artist text," +
				            "song_id uuid," +
				            "PRIMARY KEY (id, title, album, artist)" +
				            ");");


	   }
	   public void loadData() {
		   session.execute(
				      "INSERT INTO mykeyspace.songs (id, title, album, artist, tags) " +
				      "VALUES (" +
				          "756716f7-2e54-4715-9f00-91dcbea6cf50," +
				          "'La Petite Tonkinoise'," +
				          "'Bye Bye Blackbird'," +
				          "'Josephine Baker'," +
				          "{'jazz', '2013'})" +
				          ";");
				session.execute(
				      "INSERT INTO mykeyspace.playlists (id, song_id, title, album, artist) " +
				      "VALUES (" +
				          "2cc9ccb7-6221-4ccb-8387-f22b6a1b354d," +
				          "756716f7-2e54-4715-9f00-91dcbea6cf50," +
				          "'La Petite Tonkinoise'," +
				          "'Bye Bye Blackbird'," +
				          "'Josephine Baker'" +
				          ");");

	   }
	   public void querySchema(){
		   ResultSet results = session.execute("SELECT * FROM mykeyspace.playlists " +
			        "WHERE id = 2cc9ccb7-6221-4ccb-8387-f22b6a1b354d;");
		   System.out.println(String.format("%-30s\t%-20s\t%-20s\n%s", "title", "album", "artist",
			    	  "-------------------------------+-----------------------+--------------------"));
		   for (Row row : results) {
			    System.out.println(String.format("%-30s\t%-20s\t%-20s", row.getString("title"),
			    row.getString("album"),  row.getString("artist")));
			}
			System.out.println();


	   }
	   
	   public void close() {
	      cluster.close(); // .shutdown();
	   }

	   public static void main(String[] args) {
	      CQLClient client = new CQLClient();
	      client.connect("127.0.0.1");
	      client.createSchema();
           client.loadData();
	      client.querySchema();
	      client.close();
	   }
	}