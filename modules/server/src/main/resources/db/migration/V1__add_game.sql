CREATE TYPE esrb_rating AS ENUM(
  'everyone',
  'everyone-10-plus',
  'teen',
  'mature',
  'adults-only',
  'rating-pending'
);

CREATE TABLE games (
id integer NOT NULL,
slug text NOT NULL,
name text NOT NULL,
released date NOT NULL,
tba boolean NOT NULL,
rating integer NOT NULL,
rating_top integer NOT NULL,
ratings_count integer NOT NULL,
metacritic integer NOT NULL,
playtime integer NOT NULL,
updated timestamp NOT NULL,
reviews_count integer NOT NULL,
platform_slug text NOT NULL,
platform_released_at text,
parent_platforms text[] NOT NULL,
genres text[] NOT NULL,
stores text[] NOT NULL,
tags text[] NOT NULL,
esrb_rating esrb_rating,
--ratings
exceptional_rated_count integer NOT NULL,
exceptional_rated_percent float NOT NULL,
recommended_rated_count integer NOT NULL,
recommended_rated_percent float NOT NULL,
meh_rated_count integer NOT NULL,
meh_rated_percent float NOT NULL,
skip_rated_count integer NOT NULL,
skip_rated_percent float NOT NULL
);

