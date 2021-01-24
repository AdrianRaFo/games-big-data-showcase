CREATE TYPE esrb_rating AS ENUM(
  'everyone',
  'everyone-10-plus',
  'teen',
  'mature',
  'adults-only',
  'rating-pending'
);

CREATE TYPE rating_slug AS ENUM(
  'recommended',
  'exceptional',
  'meh',
  'skip'
);

CREATE TYPE game_ratings AS (
slug rating_slug,
total integer,
percent numeric
);

CREATE TABLE games (
id integer NOT NULL,
slug text NOT NULL,
name text NOT NULL,
released date NOT NULL,
tba boolean NOT NULL,
rating integer NOT NULL,
ratingTop integer NOT NULL,
ratings game_ratings[],
ratingsCount integer NOT NULL,
metacritic integer NOT NULL,
playtime integer NOT NULL,
updated timestamp NOT NULL,
reviewsCount integer NOT NULL,
platform text[],
parentPlatforms text[],
genres text[],
stores text[],
tags text[],
esrb_rating esrb_rating
);

