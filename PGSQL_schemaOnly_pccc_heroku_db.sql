--
-- PostgreSQL database dump
--

-- Dumped from database version 10.3
-- Dumped by pg_dump version 10.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: pccc_schema; Type: SCHEMA; Schema: -; Owner: qzobkerqwqeldi
--

CREATE SCHEMA pccc_schema;


ALTER SCHEMA pccc_schema OWNER TO qzobkerqwqeldi;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: capGeminiTeam; Type: TABLE; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

CREATE TABLE pccc_schema."capGeminiTeam" (
    "teamName" text NOT NULL,
    leader text,
    pk integer NOT NULL
);


ALTER TABLE pccc_schema."capGeminiTeam" OWNER TO qzobkerqwqeldi;

--
-- Name: capGeminiTeam_pk_seq; Type: SEQUENCE; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

CREATE SEQUENCE pccc_schema."capGeminiTeam_pk_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pccc_schema."capGeminiTeam_pk_seq" OWNER TO qzobkerqwqeldi;

--
-- Name: capGeminiTeam_pk_seq; Type: SEQUENCE OWNED BY; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER SEQUENCE pccc_schema."capGeminiTeam_pk_seq" OWNED BY pccc_schema."capGeminiTeam".pk;


--
-- Name: contribution; Type: TABLE; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

CREATE TABLE pccc_schema.contribution (
    synopsis text NOT NULL,
    interest text,
    usage text NOT NULL,
    pk integer NOT NULL,
    "binaryContent" bytea,
    "resourceFK" integer
);


ALTER TABLE pccc_schema.contribution OWNER TO qzobkerqwqeldi;

--
-- Name: TABLE contribution; Type: COMMENT; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

COMMENT ON TABLE pccc_schema.contribution IS 'bytea max size (recommanded) : 200Mo';


--
-- Name: contribution_pk_seq; Type: SEQUENCE; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

CREATE SEQUENCE pccc_schema.contribution_pk_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pccc_schema.contribution_pk_seq OWNER TO qzobkerqwqeldi;

--
-- Name: contribution_pk_seq; Type: SEQUENCE OWNED BY; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER SEQUENCE pccc_schema.contribution_pk_seq OWNED BY pccc_schema.contribution.pk;


--
-- Name: expertise; Type: TABLE; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

CREATE TABLE pccc_schema.expertise (
    tag integer NOT NULL,
    "expertiseLevelFK" integer NOT NULL,
    pk integer NOT NULL,
    "resourceFK" integer
);


ALTER TABLE pccc_schema.expertise OWNER TO qzobkerqwqeldi;

--
-- Name: expertiseLevel; Type: TABLE; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

CREATE TABLE pccc_schema."expertiseLevel" (
    name text NOT NULL,
    pk integer NOT NULL,
    "rbcColorName" text NOT NULL,
    "rbcColor" text NOT NULL
);


ALTER TABLE pccc_schema."expertiseLevel" OWNER TO qzobkerqwqeldi;

--
-- Name: expertiseLevel_pk_seq; Type: SEQUENCE; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

CREATE SEQUENCE pccc_schema."expertiseLevel_pk_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pccc_schema."expertiseLevel_pk_seq" OWNER TO qzobkerqwqeldi;

--
-- Name: expertiseLevel_pk_seq; Type: SEQUENCE OWNED BY; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER SEQUENCE pccc_schema."expertiseLevel_pk_seq" OWNED BY pccc_schema."expertiseLevel".pk;


--
-- Name: expertise_pk_seq; Type: SEQUENCE; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

CREATE SEQUENCE pccc_schema.expertise_pk_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pccc_schema.expertise_pk_seq OWNER TO qzobkerqwqeldi;

--
-- Name: expertise_pk_seq; Type: SEQUENCE OWNED BY; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER SEQUENCE pccc_schema.expertise_pk_seq OWNED BY pccc_schema.expertise.pk;


--
-- Name: profession; Type: TABLE; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

CREATE TABLE pccc_schema.profession (
    name text NOT NULL,
    pk integer NOT NULL
);


ALTER TABLE pccc_schema.profession OWNER TO qzobkerqwqeldi;

--
-- Name: resource; Type: TABLE; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

CREATE TABLE pccc_schema.resource (
    email text,
    lastname text,
    firstname text,
    worklocation integer,
    capgeminiteam integer,
    "professionFK" integer,
    pk integer NOT NULL,
    login text NOT NULL
);


ALTER TABLE pccc_schema.resource OWNER TO qzobkerqwqeldi;

--
-- Name: resource_pk_seq; Type: SEQUENCE; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

CREATE SEQUENCE pccc_schema.resource_pk_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pccc_schema.resource_pk_seq OWNER TO qzobkerqwqeldi;

--
-- Name: resource_pk_seq; Type: SEQUENCE OWNED BY; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER SEQUENCE pccc_schema.resource_pk_seq OWNED BY pccc_schema.resource.pk;


--
-- Name: role_pk_seq; Type: SEQUENCE; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

CREATE SEQUENCE pccc_schema.role_pk_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pccc_schema.role_pk_seq OWNER TO qzobkerqwqeldi;

--
-- Name: role_pk_seq; Type: SEQUENCE OWNED BY; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER SEQUENCE pccc_schema.role_pk_seq OWNED BY pccc_schema.profession.pk;


--
-- Name: tag; Type: TABLE; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

CREATE TABLE pccc_schema.tag (
    name text NOT NULL,
    "tagGroup" integer NOT NULL,
    pk integer NOT NULL
);


ALTER TABLE pccc_schema.tag OWNER TO qzobkerqwqeldi;

--
-- Name: tagGroup; Type: TABLE; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

CREATE TABLE pccc_schema."tagGroup" (
    name text NOT NULL,
    pk integer NOT NULL
);


ALTER TABLE pccc_schema."tagGroup" OWNER TO qzobkerqwqeldi;

--
-- Name: tagGroup_pk_seq; Type: SEQUENCE; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

CREATE SEQUENCE pccc_schema."tagGroup_pk_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pccc_schema."tagGroup_pk_seq" OWNER TO qzobkerqwqeldi;

--
-- Name: tagGroup_pk_seq; Type: SEQUENCE OWNED BY; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER SEQUENCE pccc_schema."tagGroup_pk_seq" OWNED BY pccc_schema."tagGroup".pk;


--
-- Name: tag_pk_seq; Type: SEQUENCE; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

CREATE SEQUENCE pccc_schema.tag_pk_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pccc_schema.tag_pk_seq OWNER TO qzobkerqwqeldi;

--
-- Name: tag_pk_seq; Type: SEQUENCE OWNED BY; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER SEQUENCE pccc_schema.tag_pk_seq OWNED BY pccc_schema.tag.pk;


--
-- Name: tag_x_contribution; Type: TABLE; Schema: pccc_schema; Owner: postgres
--

CREATE TABLE pccc_schema.tag_x_contribution (
    tag_fk integer NOT NULL,
    contribution_fk integer NOT NULL
);


ALTER TABLE pccc_schema.tag_x_contribution OWNER TO qzobkerqwqeldi;

--
-- Name: workLocation; Type: TABLE; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

CREATE TABLE pccc_schema."workLocation" (
    pk integer NOT NULL,
    city text,
    address text
);


ALTER TABLE pccc_schema."workLocation" OWNER TO qzobkerqwqeldi;

--
-- Name: workLocation_pk_seq; Type: SEQUENCE; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

CREATE SEQUENCE pccc_schema."workLocation_pk_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pccc_schema."workLocation_pk_seq" OWNER TO qzobkerqwqeldi;

--
-- Name: workLocation_pk_seq; Type: SEQUENCE OWNED BY; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER SEQUENCE pccc_schema."workLocation_pk_seq" OWNED BY pccc_schema."workLocation".pk;


--
-- Name: capGeminiTeam pk; Type: DEFAULT; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER TABLE ONLY pccc_schema."capGeminiTeam" ALTER COLUMN pk SET DEFAULT nextval('pccc_schema."capGeminiTeam_pk_seq"'::regclass);


--
-- Name: contribution pk; Type: DEFAULT; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER TABLE ONLY pccc_schema.contribution ALTER COLUMN pk SET DEFAULT nextval('pccc_schema.contribution_pk_seq'::regclass);


--
-- Name: expertise pk; Type: DEFAULT; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER TABLE ONLY pccc_schema.expertise ALTER COLUMN pk SET DEFAULT nextval('pccc_schema.expertise_pk_seq'::regclass);


--
-- Name: expertiseLevel pk; Type: DEFAULT; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER TABLE ONLY pccc_schema."expertiseLevel" ALTER COLUMN pk SET DEFAULT nextval('pccc_schema."expertiseLevel_pk_seq"'::regclass);


--
-- Name: profession pk; Type: DEFAULT; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER TABLE ONLY pccc_schema.profession ALTER COLUMN pk SET DEFAULT nextval('pccc_schema.role_pk_seq'::regclass);


--
-- Name: resource pk; Type: DEFAULT; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER TABLE ONLY pccc_schema.resource ALTER COLUMN pk SET DEFAULT nextval('pccc_schema.resource_pk_seq'::regclass);


--
-- Name: tag pk; Type: DEFAULT; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER TABLE ONLY pccc_schema.tag ALTER COLUMN pk SET DEFAULT nextval('pccc_schema.tag_pk_seq'::regclass);


--
-- Name: tagGroup pk; Type: DEFAULT; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER TABLE ONLY pccc_schema."tagGroup" ALTER COLUMN pk SET DEFAULT nextval('pccc_schema."tagGroup_pk_seq"'::regclass);


--
-- Name: workLocation pk; Type: DEFAULT; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER TABLE ONLY pccc_schema."workLocation" ALTER COLUMN pk SET DEFAULT nextval('pccc_schema."workLocation_pk_seq"'::regclass);


--
-- Data for Name: capGeminiTeam; Type: TABLE DATA; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

COPY pccc_schema."capGeminiTeam" ("teamName", leader, pk) FROM stdin;
code breakers	Mathieu Noyaret	1
code surfers	Mathieu Noyaret	2
\.


--
-- Data for Name: contribution; Type: TABLE DATA; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

COPY pccc_schema.contribution (synopsis, interest, usage, pk, "binaryContent", "resourceFK") FROM stdin;
contribution1	interêt à décrire	usage à décrire	4	\N	3
contribution 2	interêt à décrire	usage à décrire	5	\N	3
contribution 3	interêt à décrire	usage à décrire	6	\N	3
créer le champ timeInMilliSinceMidnigth (type ES : long)	facilite les aggregations \n\t\t\tdes 5 dernières minutes sur 7 derniers jours	dans tous les index	3	\N	3
\.


--
-- Data for Name: expertise; Type: TABLE DATA; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

COPY pccc_schema.expertise (tag, "expertiseLevelFK", pk, "resourceFK") FROM stdin;
13	3	11	3
2	2	1	\N
22	2	8	3
23	2	9	3
13	2	10	3
25	3	5	3
25	3	6	4
\.


--
-- Data for Name: expertiseLevel; Type: TABLE DATA; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

COPY pccc_schema."expertiseLevel" (name, pk, "rbcColorName", "rbcColor") FROM stdin;
expert	2	ORANGE	#FC9D05
Contributeur	1	RED	#FF0000
avancé	3	GREEN	#548C3F
cherche un sujet sur	6	LIGHT_GREEN	#D0F2C9
basique	4	LIGHT_GRAY	#D6D8D8
intéressé par	5	LIGHT_YELLOW	#BFAFA8
\.


--
-- Data for Name: profession; Type: TABLE DATA; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

COPY pccc_schema.profession (name, pk) FROM stdin;
Team Leader	2
Développeur Junior	3
Développeur Senior	4
Project Manager	5
Architecte	6
\.


--
-- Data for Name: resource; Type: TABLE DATA; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

COPY pccc_schema.resource (email, lastname, firstname, worklocation, capgeminiteam, "professionFK", pk, login) FROM stdin;
sebastien.flacher@capgemini.fr	Flacher	Sebastien	1	1	6	3	sflacher
mathieu.nogaret@capgemini.fr	Nogaret	Mathieu	1	1	4	4	mnogaret
\.


--
-- Data for Name: tag; Type: TABLE DATA; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

COPY pccc_schema.tag (name, "tagGroup", pk) FROM stdin;
javassist	4	2
spring	4	3
spring bootstrap	4	4
axis2	4	5
quartz	4	6
lucène	4	7
maven	4	8
supervision flux	1	9
exploitation serveurs	1	10
génération métriques	1	11
génération métadonnées	1	12
API Mgt	1	13
micro services	1	14
machine learning -- IA	1	15
sécurité / encryption	1	16
mobilité	1	18
elasticsearch	3	19
jenkins	3	20
grafana	3	21
docker	3	22
SQL server	3	23
Oracle Bdd	3	24
Oracle J2ee	3	25
postgresql	3	26
\.


--
-- Data for Name: tagGroup; Type: TABLE DATA; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

COPY pccc_schema."tagGroup" (name, pk) FROM stdin;
Domaines	1
Langages	2
Application	3
Frameworks	4
Dev Tools	5
\.


--
-- Data for Name: tag_x_contribution; Type: TABLE DATA; Schema: pccc_schema; Owner: postgres
--

COPY pccc_schema.tag_x_contribution (tag_fk, contribution_fk) FROM stdin;
26	6
24	6
22	6
20	6
10	6
8	6
2	6
2	4
2	5
8	5
10	5
12	5
12	4
\.


--
-- Data for Name: workLocation; Type: TABLE DATA; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

COPY pccc_schema."workLocation" (pk, city, address) FROM stdin;
1	Lyon	 43 Rue Pré-Gaudry, 69007 Lyon
\.


--
-- Name: capGeminiTeam_pk_seq; Type: SEQUENCE SET; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

SELECT pg_catalog.setval('pccc_schema."capGeminiTeam_pk_seq"', 2, true);


--
-- Name: contribution_pk_seq; Type: SEQUENCE SET; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

SELECT pg_catalog.setval('pccc_schema.contribution_pk_seq', 6, true);


--
-- Name: expertiseLevel_pk_seq; Type: SEQUENCE SET; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

SELECT pg_catalog.setval('pccc_schema."expertiseLevel_pk_seq"', 6, true);


--
-- Name: expertise_pk_seq; Type: SEQUENCE SET; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

SELECT pg_catalog.setval('pccc_schema.expertise_pk_seq', 11, true);


--
-- Name: resource_pk_seq; Type: SEQUENCE SET; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

SELECT pg_catalog.setval('pccc_schema.resource_pk_seq', 4, true);


--
-- Name: role_pk_seq; Type: SEQUENCE SET; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

SELECT pg_catalog.setval('pccc_schema.role_pk_seq', 6, true);


--
-- Name: tagGroup_pk_seq; Type: SEQUENCE SET; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

SELECT pg_catalog.setval('pccc_schema."tagGroup_pk_seq"', 5, true);


--
-- Name: tag_pk_seq; Type: SEQUENCE SET; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

SELECT pg_catalog.setval('pccc_schema.tag_pk_seq', 27, true);


--
-- Name: workLocation_pk_seq; Type: SEQUENCE SET; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

SELECT pg_catalog.setval('pccc_schema."workLocation_pk_seq"', 1, true);


--
-- Name: capGeminiTeam capGeminiTeam_pk; Type: CONSTRAINT; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER TABLE ONLY pccc_schema."capGeminiTeam"
    ADD CONSTRAINT "capGeminiTeam_pk" PRIMARY KEY (pk);


--
-- Name: contribution contribution_pk; Type: CONSTRAINT; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER TABLE ONLY pccc_schema.contribution
    ADD CONSTRAINT contribution_pk PRIMARY KEY (pk);


--
-- Name: expertiseLevel expLevel_pk; Type: CONSTRAINT; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER TABLE ONLY pccc_schema."expertiseLevel"
    ADD CONSTRAINT "expLevel_pk" PRIMARY KEY (pk);


--
-- Name: expertise expertise_pk; Type: CONSTRAINT; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER TABLE ONLY pccc_schema.expertise
    ADD CONSTRAINT expertise_pk PRIMARY KEY (pk);


--
-- Name: resource resource_pk; Type: CONSTRAINT; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER TABLE ONLY pccc_schema.resource
    ADD CONSTRAINT resource_pk PRIMARY KEY (pk);


--
-- Name: profession role_pkey; Type: CONSTRAINT; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER TABLE ONLY pccc_schema.profession
    ADD CONSTRAINT role_pkey PRIMARY KEY (pk);


--
-- Name: tagGroup tagGroup_pkey; Type: CONSTRAINT; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER TABLE ONLY pccc_schema."tagGroup"
    ADD CONSTRAINT "tagGroup_pkey" PRIMARY KEY (pk);


--
-- Name: tag tag_pk; Type: CONSTRAINT; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER TABLE ONLY pccc_schema.tag
    ADD CONSTRAINT tag_pk PRIMARY KEY (pk);


--
-- Name: workLocation workLocation_pkey; Type: CONSTRAINT; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER TABLE ONLY pccc_schema."workLocation"
    ADD CONSTRAINT "workLocation_pkey" PRIMARY KEY (pk);


--
-- Name: fki_capgeminiteam_fk; Type: INDEX; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

CREATE INDEX fki_capgeminiteam_fk ON pccc_schema.resource USING btree (capgeminiteam);


--
-- Name: fki_contribution_resource_fk; Type: INDEX; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

CREATE INDEX fki_contribution_resource_fk ON pccc_schema.contribution USING btree ("resourceFK");


--
-- Name: fki_contribution_x_fk; Type: INDEX; Schema: pccc_schema; Owner: postgres
--

CREATE INDEX fki_contribution_x_fk ON pccc_schema.tag_x_contribution USING btree (contribution_fk);


--
-- Name: fki_expertiseLevel_fk; Type: INDEX; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

CREATE INDEX "fki_expertiseLevel_fk" ON pccc_schema.expertise USING btree ("expertiseLevelFK");


--
-- Name: fki_expertise_resource_fk; Type: INDEX; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

CREATE INDEX fki_expertise_resource_fk ON pccc_schema.expertise USING btree ("resourceFK");


--
-- Name: fki_profession_fk; Type: INDEX; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

CREATE INDEX fki_profession_fk ON pccc_schema.resource USING btree ("professionFK");


--
-- Name: fki_tagGroupFK; Type: INDEX; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

CREATE INDEX "fki_tagGroupFK" ON pccc_schema.tag USING btree ("tagGroup");


--
-- Name: fki_tag_fk; Type: INDEX; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

CREATE INDEX fki_tag_fk ON pccc_schema.expertise USING btree (tag);


--
-- Name: fki_tag_x_fk; Type: INDEX; Schema: pccc_schema; Owner: postgres
--

CREATE INDEX fki_tag_x_fk ON pccc_schema.tag_x_contribution USING btree (tag_fk);


--
-- Name: fki_worklocation_fk; Type: INDEX; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

CREATE INDEX fki_worklocation_fk ON pccc_schema.resource USING btree (worklocation);


--
-- Name: resource capgeminiteam_fk; Type: FK CONSTRAINT; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER TABLE ONLY pccc_schema.resource
    ADD CONSTRAINT capgeminiteam_fk FOREIGN KEY (capgeminiteam) REFERENCES pccc_schema."capGeminiTeam"(pk) MATCH FULL ON DELETE SET NULL;


--
-- Name: contribution contribution_resource_fk; Type: FK CONSTRAINT; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER TABLE ONLY pccc_schema.contribution
    ADD CONSTRAINT contribution_resource_fk FOREIGN KEY ("resourceFK") REFERENCES pccc_schema.resource(pk) MATCH FULL ON DELETE SET NULL;


--
-- Name: tag_x_contribution contribution_x_fk; Type: FK CONSTRAINT; Schema: pccc_schema; Owner: postgres
--

ALTER TABLE ONLY pccc_schema.tag_x_contribution
    ADD CONSTRAINT contribution_x_fk FOREIGN KEY (contribution_fk) REFERENCES pccc_schema.contribution(pk) MATCH FULL ON DELETE CASCADE;


--
-- Name: expertise expertiseLevel_fk; Type: FK CONSTRAINT; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER TABLE ONLY pccc_schema.expertise
    ADD CONSTRAINT "expertiseLevel_fk" FOREIGN KEY ("expertiseLevelFK") REFERENCES pccc_schema."expertiseLevel"(pk) MATCH FULL ON DELETE SET NULL;


--
-- Name: expertise expertise_resource_fk; Type: FK CONSTRAINT; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER TABLE ONLY pccc_schema.expertise
    ADD CONSTRAINT expertise_resource_fk FOREIGN KEY ("resourceFK") REFERENCES pccc_schema.resource(pk) MATCH FULL ON DELETE SET NULL;


--
-- Name: resource profession_fk; Type: FK CONSTRAINT; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER TABLE ONLY pccc_schema.resource
    ADD CONSTRAINT profession_fk FOREIGN KEY ("professionFK") REFERENCES pccc_schema.profession(pk) MATCH FULL ON DELETE SET NULL;


--
-- Name: tag tagGroup_fk; Type: FK CONSTRAINT; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER TABLE ONLY pccc_schema.tag
    ADD CONSTRAINT "tagGroup_fk" FOREIGN KEY ("tagGroup") REFERENCES pccc_schema."tagGroup"(pk) MATCH FULL ON DELETE SET NULL;


--
-- Name: expertise tag_fk; Type: FK CONSTRAINT; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER TABLE ONLY pccc_schema.expertise
    ADD CONSTRAINT tag_fk FOREIGN KEY (tag) REFERENCES pccc_schema.tag(pk) MATCH FULL ON DELETE SET NULL;


--
-- Name: tag_x_contribution tag_fk; Type: FK CONSTRAINT; Schema: pccc_schema; Owner: postgres
--

ALTER TABLE ONLY pccc_schema.tag_x_contribution
    ADD CONSTRAINT tag_fk FOREIGN KEY (tag_fk) REFERENCES pccc_schema.tag(pk) MATCH FULL ON DELETE CASCADE;


--
-- Name: resource worklocation_fk; Type: FK CONSTRAINT; Schema: pccc_schema; Owner: qzobkerqwqeldi
--

ALTER TABLE ONLY pccc_schema.resource
    ADD CONSTRAINT worklocation_fk FOREIGN KEY (worklocation) REFERENCES pccc_schema."workLocation"(pk) MATCH FULL ON DELETE SET NULL;


--
-- PostgreSQL database dump complete
--

