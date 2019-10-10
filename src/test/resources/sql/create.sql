create table trips (
    id bigint auto_increment primary key,
    vendor_id varchar(16),
    start_time varchar(24),
    end_time varchar(24),
    passenger_count int,
    trip_distance double,
    rate_code int,
    store_fwd_flag char,
    pickup_location_id int,
    dropoff_location_id int,
    payment_type int,
    fare_amount double,
    extra double,
    mta_tax double,
    tip_amount double,
    toll_amount double,
    improvement_surcharge double,
    total_amount double,
    congestion_surcharge double
);