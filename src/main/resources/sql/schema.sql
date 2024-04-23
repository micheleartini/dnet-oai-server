CREATE TABLE oai_data(
	id      text PRIMARY KEY,
	body    bytea,
	date    timestamp NOT NULL DEFAULT now(),
	sets    text[]    NOT NULL DEFAULT '{}'
);
