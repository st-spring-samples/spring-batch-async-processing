#! /bin/bash

WORK_DIR="$HOME"
RIDE_INFO_FILE="$WORK_DIR/rideinfo.csv"
H2_ZIP_NAME="$WORK_DIR/h2-2019-03-13.zip"
H2_DATA_DIR="$WORK_DIR/h2-data"

download_file()
{
    FILE=$1
    if [ ! -f "$FILE" ]; then
        echo "File '$FILE' doesn't exist. Downloading..."
        curl -o $FILE $2
    else
        echo "File '$FILE' already exists"
    fi
}

# Download ride info file
download_file $RIDE_INFO_FILE https://s3.amazonaws.com/nyc-tlc/trip+data/yellow_tripdata_2019-01.csv
# Download H2 database
download_file $H2_ZIP_NAME http://www.h2database.com/h2-2019-03-13.zip
# Create H2 data directory
if [ ! -d "$H2_DATA_DIR" ]; then
    echo "Creating H2 data foler"
    mkdir $H2_DATA_DIR
fi
# Extract H2 database
if [ ! -d "h2" ]; then
    echo "Extracting H2 database..."
    unzip $H2_ZIP_NAME -d $WORK_DIR/
fi

# Launch H2 database, run batch and kill H2 process
java -cp $WORK_DIR/h2/bin/h2-1.4.199.jar org.h2.tools.Server -tcp -web -ifNotExists &
pid=$! &&
echo "H2 process id: $pid" &&
java -cp $WORK_DIR/h2/bin/h2-1.4.199.jar org.h2.tools.RunScript \
-url "jdbc:h2:tcp://127.0.1.1:9092/$WORK_DIR/h2-data/yellow-taxi" \
-user sa -password "" \
-script src/test/resources/sql/create.sql &&
echo "Database tables created successfully"
java -jar target/spring-batch-async-processing-0.0.1-SNAPSHOT.jar \
$RIDE_INFO_FILE \
--thread.count=4 \
--spring.datasource.url="jdbc:h2:tcp://127.0.1.1:9092/$WORK_DIR/h2-data/yellow-taxi" &&
kill $pid