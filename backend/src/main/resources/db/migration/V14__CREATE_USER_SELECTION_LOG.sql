CREATE TABLE user_selection_log (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    store_id INTEGER NOT NULL,
    good_id INTEGER NOT NULL,
    price INTEGER NOT NULL,
    preference_type VARCHAR(10) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW(),

    CONSTRAINT user_selection_)log_user_id_fkey
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);