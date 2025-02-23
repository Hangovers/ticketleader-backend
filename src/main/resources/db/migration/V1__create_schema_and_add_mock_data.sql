CREATE TABLE venue (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        address VARCHAR(255) NOT NULL,
                        description TEXT NULL
);

CREATE TABLE sector (
                         id BIGSERIAL PRIMARY KEY,
                         venue_id BIGINT NOT NULL REFERENCES venue(id),
                         name VARCHAR(255) NOT NULL,
                         total_capacity INTEGER NOT NULL,
                         price DECIMAL(10, 2) NOT NULL,
                         type VARCHAR(50) NOT NULL,
                         description VARCHAR(255) NOT NULL,
                         seats_per_row INTEGER NOT NULL
);

CREATE TABLE event (
                        id BIGSERIAL PRIMARY KEY,
                        venue_id BIGINT NOT NULL REFERENCES venue(id),
                        title VARCHAR(255) NOT NULL,
                        event_date TIMESTAMP NOT NULL,
                        capacity INTEGER NOT NULL
);

CREATE TABLE reservee (
                           id BIGSERIAL PRIMARY KEY,
                           first_name VARCHAR(100) NOT NULL,
                           last_name VARCHAR(100) NOT NULL,
                           email VARCHAR(255) NOT NULL UNIQUE,
                           username VARCHAR(100) NULL,
                           password VARCHAR(255) NULL,
                           gender VARCHAR(50) NULL
);

CREATE TABLE seat (
                       id BIGSERIAL PRIMARY KEY,
                       sector_id BIGINT NOT NULL REFERENCES sector(id),
                       seat_number INTEGER NOT NULL
);

CREATE TABLE reservation (
                              id BIGSERIAL PRIMARY KEY,
                              event_id BIGINT NOT NULL REFERENCES event(id),
                              sector_id BIGINT NOT NULL REFERENCES sector(id),
                              reservee_id BIGINT NOT NULL REFERENCES reservee(id),
                              seat_id BIGINT REFERENCES seat(id),
                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO venue (name, address, description) VALUES
                                                    ('Grand Hall', '123 Main St', 'A grand hall for concerts and events.'),
                                                    ('City Arena', '456 Oak Ave', 'A large arena for sports and performances.'),
                                                    ('Open Air Theater', '789 Pine Ln', 'An outdoor theater for plays and shows.'),
                                                    ('Downtown Center', '101 Elm St', 'A central hub for various events.'),
                                                    ('Exhibition Stadium', '202 Maple Ave', 'A stadium for large-scale exhibitions and concerts.');

INSERT INTO sector (venue_id, name, total_capacity, price, type, description, seats_per_row) VALUES
                                                                                                  (1, 'Grand Hall - Sector A', 100, 50.00, 'Seated', 'Front section', 10),
                                                                                                  (1, 'Grand Hall - Sector B', 150, 40.00, 'Seated', 'Middle section', 15),
                                                                                                  (1, 'Grand Hall - Sector C', 200, 30.00, 'Standing', 'Back section', 0),
                                                                                                  (2, 'City Arena - Sector A', 200, 60.00, 'Seated', 'Lower level', 20),
                                                                                                  (2, 'City Arena - Sector B', 250, 50.00, 'Seated', 'Upper level', 25),
                                                                                                  (2, 'City Arena - Sector C', 300, 40.00, 'Standing', 'Standing area', 0),
                                                                                                  (3, 'Open Air Theater - Sector A', 150, 55.00, 'Seated', 'Front rows', 15),
                                                                                                  (3, 'Open Air Theater - Sector B', 200, 45.00, 'Seated', 'Back rows', 20),
                                                                                                  (3, 'Open Air Theater - Sector C', 250, 35.00, 'Standing', 'Lawn area', 0),
                                                                                                  (4, 'Downtown Center - Sector A', 100, 70.00, 'Seated', 'VIP section', 10),
                                                                                                  (4, 'Downtown Center - Sector B', 150, 60.00, 'Seated', 'Regular section', 15),
                                                                                                  (4, 'Downtown Center - Sector C', 200, 50.00, 'Standing', 'Standing room', 0),
                                                                                                  (5, 'Exhibition Stadium - Sector A', 300, 80.00, 'Seated', 'Field level', 30),
                                                                                                  (5, 'Exhibition Stadium - Sector B', 350, 70.00, 'Seated', 'Mezzanine', 35),
                                                                                                  (5, 'Exhibition Stadium - Sector C', 400, 60.00, 'Standing', 'General admission', 0);

INSERT INTO seat (sector_id, seat_number)
SELECT s.id, gs.seat_no
FROM sector s
         CROSS JOIN generate_series(1, 25) AS gs(seat_no)
WHERE s.type = 'Seated';

INSERT INTO event (venue_id, title, event_date, capacity) VALUES
                                                               (1, 'Concert Night', CURRENT_DATE + INTERVAL '1 day', 150),
                                                               (1, 'Comedy Show', CURRENT_DATE + INTERVAL '2 days', 100),
                                                               (2, 'Sports Event', CURRENT_DATE + INTERVAL '3 days', 250),
                                                               (2, 'Theater Performance', CURRENT_DATE + INTERVAL '4 days', 200),
                                                               (3, 'Festival', CURRENT_DATE + INTERVAL '5 days', 300),
                                                               (3, 'Opera Night', CURRENT_DATE + INTERVAL '6 days', 150),
                                                               (4, 'Dance Show', CURRENT_DATE + INTERVAL '7 days', 100),
                                                               (4, 'Magic Show', CURRENT_DATE + INTERVAL '8 days', 150),
                                                               (5, 'Rock Concert', CURRENT_DATE + INTERVAL '9 days', 350),
                                                               (5, 'Stand-up Comedy', CURRENT_DATE + INTERVAL '10 days', 400);