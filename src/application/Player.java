package application;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import com.mpatric.mp3agic.*;


public class Player extends Application /*implements Observer*/{

	//private Subject picDir;
	//private Subject musicDir;
	private Stage window;
	private Scene scene;
	private BorderPane bPane;
	private HBox topButtons;
	private MediaView mediaView;
	private TableView<File> table;
	private ChangeListener<Duration> progressChangeListener;
	private List<MediaPlayer> players;
	private Label album= new Label("-");
	private Label artist= new Label("-");
	private Label title= new Label("-");
	private Label year= new Label("-");
	private Image img = new Image("file:question.png");
	private ImageView albumCover = new ImageView(img);
	private Label currentlyPlaying = new Label("-");
	private final ProgressBar progress = new ProgressBar();

	@Override
	public void start(Stage primaryStage) {

		window = primaryStage;
		window.setTitle("Pete's Media Player");
		window.getIcons().add(new Image("file:icon.png"));

		bPane = new BorderPane();

		Button musicButton = new Button("Music");
		musicButton.setGraphic(new ImageView(new Image("file:music.png")));
		musicButton.setOnAction(e->{try {
			setMusicScene();
		} catch (UnsupportedTagException e1) {
			e1.printStackTrace();
		} catch (InvalidDataException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}});

		Button pictureButton = new Button ("Pictures");
		pictureButton.setGraphic(new ImageView(new Image("file:pic.png")));
		pictureButton.setOnAction(e->{setPictureScene();});

		topButtons = new HBox();
		topButtons.getChildren().addAll(musicButton,pictureButton);
		topButtons.setAlignment(Pos.CENTER);
		topButtons.setPadding(new Insets(2, 0, 0, 0));
		topButtons.setSpacing(10);

		bPane.setTop(topButtons);

		BorderPane chatPane = new BorderPane();
		Text chatTitle = new Text("CHAT CLIENT:");
		chatTitle.setFont(Font.font("default", FontWeight.BOLD, 20));
		chatPane.setTop(chatTitle);
		chatPane.setPadding(new Insets(10, 10, 10, 10));
		chatPane.setPrefWidth(300);
		new ChatClient(chatPane);
		bPane.setRight(chatPane);

		scene = new Scene(bPane, 1200, 800);
		bPane.prefHeightProperty().bind(scene.heightProperty());
		bPane.prefWidthProperty().bind(scene.widthProperty());
		try {
			setMusicScene();
		} catch (UnsupportedTagException e1) {
			e1.printStackTrace();
		} catch (InvalidDataException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		window.setScene(scene);
		window.show();

	}

	void setPictureScene() {


		try{
			mediaView.getMediaPlayer().stop();
		}catch(NullPointerException e){
			//Carry on regardless!
		}


		ScrollPane root = new ScrollPane();
		TilePane tile = new TilePane();
		root.setStyle("-fx-background-color: DAE6F3;");
		tile.setPadding(new Insets(15, 15, 15, 15));
		tile.setHgap(15);
		tile.setVgap(15);

		String path = "C:\\Pics";
		/*WatchDir watch = new WatchDir(path);
		Thread tPic = new Thread(watch);
		tPic.start();*/


		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		for (final File file : listOfFiles) {
			ImageView imageView;
			imageView = createImageView(file);
			tile.getChildren().addAll(imageView);
		}


		root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Horizontal
		root.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scroll bar
		root.setFitToWidth(true);
		root.setContent(tile);

		bPane.setCenter(root);

	}

	private ImageView createImageView(final File imageFile) {

		ImageView imageView = null;
		try {
			final Image image = new Image(new FileInputStream(imageFile), 150, 0, true,
					true);
			imageView = new ImageView(image);
			imageView.setFitWidth(150);
			imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent mouseEvent) {

					if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){

						if(mouseEvent.getClickCount() == 2){
							try {
								BorderPane borderPane = new BorderPane();
								ImageView imageView = new ImageView();
								Image image = new Image(new FileInputStream(imageFile));
								imageView.setImage(image);
								imageView.setStyle("-fx-background-color: BLACK");
								imageView.setFitHeight(bPane.getHeight() - 10);
								imageView.setFitWidth(bPane.getWidth() - 10);
								imageView.setPreserveRatio(true);
								imageView.setSmooth(true);
								imageView.setCache(true);

								Image saveImage = new Image("file:save.png");
								Button saveButton = new Button();
								saveButton.setGraphic(new ImageView(saveImage));
								saveButton.setOnAction(e -> {
									FileChooser fileChooser = new FileChooser();

									//Set extension filter
									FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Picture files (*.jpg)", "*.jpg");
									fileChooser.getExtensionFilters().add(extFilter);
									fileChooser.setInitialFileName(imageFile.getName());
									//Show save file dialog
									File dir = fileChooser.showSaveDialog(new Stage() );
									Runnable runnable = () -> {
										if(dir != null){
											File fileToCopy = imageFile;
											new CopyFactory(dir, fileToCopy);
										}
									};
									Thread thread = new Thread(runnable);
									thread.start();
								});


								borderPane.setBottom(saveButton);
								borderPane.setCenter(imageView);
								borderPane.setStyle("-fx-background-color: BLACK");
								Stage newStage = new Stage();
								newStage.setWidth(window.getWidth());
								newStage.setHeight(window.getHeight());
								newStage.setTitle(imageFile.getName());
								Scene scene = new Scene(borderPane,Color.BLACK);
								newStage.setScene(scene);
								newStage.show();
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}

						}
					}
				}
			});
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		return imageView;

	}

	void setMusicScene() throws UnsupportedTagException, InvalidDataException, IOException {

		String path = "C:\\Songs";
		File dir = new File(path);
		//WatchedDirectory mWatch = new WatchedDirectory(path);
		//mWatch.registerObserver(this);
		//setSubject(mWatch);
		/*WatchDir watch = new WatchDir(path);
		Thread tMusic = new Thread(watch);
		tMusic.start();*/
		// create some media players.
		players = new ArrayList<MediaPlayer>();
		for (String file : dir.list(new FilenameFilter() {
			@Override public boolean accept(File dir, String name) {
				return name.endsWith(".mp3");
			}
		})) players.add(createPlayer("file:///" + (dir + "\\" + file).replace("\\", "/").replaceAll(" ", "%20")));
		if (players.isEmpty()) {
			System.out.println("No audio found in " + dir);
			return;
		}

		// create a view to show the mediaplayers.
		mediaView = new MediaView(players.get(0));

		// play each audio file in turn.
		for (int i = 0; i < players.size(); i++) {
			final MediaPlayer player     = players.get(i);
			final MediaPlayer nextPlayer = players.get((i + 1) % players.size());
			player.setOnEndOfMedia(new Runnable() {
				@Override public void run() {
					player.currentTimeProperty().removeListener(progressChangeListener);
					mediaView.setMediaPlayer(nextPlayer);
					nextPlayer.play();
				}
			});


		}



		// display the name of the currently playing track.
		mediaView.mediaPlayerProperty().addListener(new ChangeListener<MediaPlayer>() {
			@Override public void changed(ObservableValue<? extends MediaPlayer> observableValue, 
					MediaPlayer oldPlayer, MediaPlayer newPlayer) {

				setCurrentlyPlaying(newPlayer);

			}
		});

		// start playing the first track.
		mediaView.setMediaPlayer(players.get(0));
		mediaView.getMediaPlayer().play();
		setCurrentlyPlaying(mediaView.getMediaPlayer());

		BorderPane musicPane = new BorderPane();
		/*musicPane.setBottom(addToolBar());*/


		BorderPane tablePane = new BorderPane();
		Text tableTitle = new Text("DIRECTORY CONTENTS:");
		tableTitle.setFont(Font.font("default", FontWeight.BOLD, 20));
		tablePane.setTop(tableTitle);
		tablePane.setPadding(new Insets(10, 10, 10, 10));
		tablePane.setPrefWidth(300);
		table = new TableView<File>();
		/*TableViewFactory tvf = */new TableViewFactory(table,dir);
		tablePane.setCenter(table);
		musicPane.setLeft(tablePane);

		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(25);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(75);
		GridPane centerPane = new GridPane();
		centerPane.getColumnConstraints().addAll(col1, col2);
		centerPane.setPadding(new Insets(10, 10, 10, 10));
		centerPane.setVgap(50);
		centerPane.setHgap(10);
		setSongLabels(centerPane);
		musicPane.setCenter(centerPane);

		bPane.setCenter(musicPane);
	}

	private void setSongLabels(GridPane centerPane) {

		currentlyPlaying.setFont(Font.font("default", FontWeight.BOLD, 20));
		GridPane.setConstraints(currentlyPlaying, 0, 0, 2, 1, HPos.CENTER, VPos.BASELINE);

		Text songDetails = new Text("SONG DETAILS:");
		songDetails.setFont(Font.font("default", FontWeight.BOLD, 20));
		GridPane.setConstraints(songDetails, 0, 1, 2, 1, HPos.CENTER, VPos.BASELINE);

		Label titleLabel = new Label("Song Title:");
		titleLabel.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(titleLabel, 0, 2,1, 1, HPos.RIGHT, VPos.BASELINE);

		title.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(title, 1, 2,1, 1, HPos.LEFT, VPos.BASELINE);

		Label artistLabel = new Label("Artist:");
		artistLabel.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(artistLabel, 0, 3,1, 1, HPos.RIGHT, VPos.BASELINE);

		artist.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(artist, 1, 3,1, 1, HPos.LEFT, VPos.BASELINE);

		Label albumLabel = new Label("Album:");
		albumLabel.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(albumLabel, 0, 4,1, 1, HPos.RIGHT, VPos.BASELINE);

		album.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(album, 1, 4,1, 1, HPos.LEFT, VPos.BASELINE);

		Label yearLabel = new Label("Year:");
		yearLabel.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(yearLabel, 0, 5,1, 1, HPos.RIGHT, VPos.BASELINE);

		year.setFont(Font.font("default", FontPosture.ITALIC, 18));
		GridPane.setConstraints(year, 1, 5,1, 1, HPos.LEFT, VPos.BASELINE);

		albumCover.setFitHeight(200);
		albumCover.setFitWidth(200);
		GridPane.setConstraints(albumCover, 0, 6,2, 1, HPos.CENTER, VPos.BASELINE);

		HBox buttons = new HBox(addToolBar());
		GridPane.setConstraints(buttons, 0, 7,2, 1, HPos.CENTER, VPos.BASELINE);

		centerPane.getChildren().addAll(currentlyPlaying, songDetails,titleLabel,artistLabel,albumLabel,yearLabel,/*artLabel*/
				album,artist,title,year,albumCover, buttons);
	}

	private HBox addToolBar() {
		HBox toolBar = new HBox();
		toolBar.setPadding(new Insets(5));
		toolBar.setAlignment(Pos.CENTER);
		toolBar.alignmentProperty().isBound();
		toolBar.setSpacing(5);

		Image saveImage = new Image("file:save.png");
		Button saveButton = new Button();
		saveButton.setGraphic(new ImageView(saveImage));
		saveButton.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();

			//Set extension filter
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Music files (*.mp3)", "*.mp3");
			fileChooser.getExtensionFilters().add(extFilter);
			fileChooser.setInitialFileName(table.getSelectionModel().getSelectedItem().getName());
			//Show save file dialog
			File dir = fileChooser.showSaveDialog(new Stage() );
			Runnable runnable = () -> {
				if(dir != null){
					File fileToCopy = table.getSelectionModel().getSelectedItem();
					new CopyFactory(dir, fileToCopy);
				}
			};
			Thread thread = new Thread(runnable);
			thread.start();
			
		});

		Image playButtonImage = new Image("file:play.png");
		Button playButton = new Button();
		playButton.setGraphic(new ImageView(playButtonImage));
		playButton.setOnAction((ActionEvent e) -> {
			mediaView.getMediaPlayer().play();
		});


		Image pausedButtonImage = new Image("file:pause.png");
		Button pauseButton = new Button();
		pauseButton.setGraphic(new ImageView(pausedButtonImage));

		pauseButton.setOnAction((ActionEvent e) -> {
			mediaView.getMediaPlayer().pause();
		});


		Image stepBackImage = new Image("file:stepback.png");
		Button stepBackButton = new Button();
		stepBackButton.setGraphic(new ImageView(stepBackImage));

		stepBackButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent actionEvent) {
				final MediaPlayer curPlayer = mediaView.getMediaPlayer();
				MediaPlayer nextPlayer = players.get((players.indexOf(curPlayer) - 1) % players.size());
				mediaView.setMediaPlayer(nextPlayer);
				curPlayer.currentTimeProperty().removeListener(progressChangeListener);
				curPlayer.stop();
				nextPlayer.play();
			}
		});


		Image stepForwardImage = new Image("file:stepforward.png");
		Button stepForwardButton = new Button();
		stepForwardButton.setGraphic(new ImageView(stepForwardImage));

		stepForwardButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent actionEvent) {
				final MediaPlayer curPlayer = mediaView.getMediaPlayer();
				MediaPlayer nextPlayer = players.get((players.indexOf(curPlayer) + 1) % players.size());
				mediaView.setMediaPlayer(nextPlayer);
				curPlayer.currentTimeProperty().removeListener(progressChangeListener);
				curPlayer.stop();
				nextPlayer.play();
			}
		});

		progress.setPrefWidth(250);
		toolBar.getChildren().addAll(saveButton,stepBackButton,playButton, pauseButton,
				stepForwardButton, progress ,mediaView);

		return toolBar;
	}

	/** @return a MediaPlayer for the given source which will report any errors it encounters */
	private MediaPlayer createPlayer(String aMediaSrc) {
		final MediaPlayer player = new MediaPlayer(new Media(aMediaSrc));
		player.setOnError(new Runnable() {
			@Override public void run() {
				System.out.println("Media error occurred: " + player.getError());
			}
		});


		return player;
	}

	/** sets the currently playing label to the label of the new media player and updates the progress monitor.  */
	private void setCurrentlyPlaying(final MediaPlayer newPlayer) {
		progress.setProgress(0);
		progressChangeListener = new ChangeListener<Duration>() {
			@Override public void changed(ObservableValue<? extends Duration> observableValue, Duration oldValue, Duration newValue) {
				progress.setProgress(1.0 * newPlayer.getCurrentTime().toMillis() / newPlayer.getTotalDuration().toMillis());
			}
		};
		newPlayer.currentTimeProperty().addListener(progressChangeListener);

		String source = newPlayer.getMedia().getSource();
		source = source.substring(0, source.length() - ".mp3".length());
		source = source.substring(source.lastIndexOf("/") + 1).replaceAll("%20", " ");
		currentlyPlaying.setText("Now Playing: " + source);


		String s = newPlayer.getMedia().getSource();
		s = s.substring(8);
		s = s.replace("/", "\\").replaceAll("%20", " ");
		ID3v2 id3v2tag = null;
		try{
			Mp3File file = new Mp3File(s);
			id3v2tag = (file).getId3v2Tag();
			byte[] imageData = id3v2tag.getAlbumImage();
			//converting the bytes to an image
			BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageData));
			Image image = SwingFXUtils.toFXImage(img, null);
			albumCover.setImage(image);

		}catch(Exception e){
			Image noCover = new Image("file:question.png");
			albumCover.setImage(noCover);
		}
		title.setText(id3v2tag.getTitle());
		artist.setText(id3v2tag.getArtist());
		album.setText(id3v2tag.getAlbum());
		year.setText(id3v2tag.getYear());
		final Reflection reflection = new Reflection();
		reflection.setFraction(0.2);
		albumCover.setEffect(reflection);

	}


	public static void main(String[] args) {
		launch(args);
	}


	/*	public void setSubject(Subject s) {
		this.setSubject(s);
	}
	@Override
	public void update() {
		Boolean b = musicDir.getUpdate();
		if(b){
			AlertBox.display("Directory Updated!", "Directory has been updated!");
			try {
				setMusicScene();
			} catch (UnsupportedTagException e1) {
				e1.printStackTrace();
			} catch (InvalidDataException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}*/




}
