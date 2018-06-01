-- https://www.postgresql.org/docs/current/static/ddl-constraints.html

-- ==== Les tables
--SELECT * FROM pccc_schema."profession";
--SELECT * FROM pccc_schema."expertiseLevel";
--SELECT * FROM pccc_schema."expertise";
--SELECT * FROM pccc_schema."resource";
--SELECT * FROM pccc_schema."contribution";
--SELECT * FROM pccc_schema."tag_x_contribution";
--SELECT * FROM pccc_schema."tag";

-- ==== Les tags et leurs groupes
SELECT tag.name as tag, "tagGroup".name as tagGroup FROM pccc_schema.tag, pccc_schema."tagGroup"
where tag."tagGroup"="tagGroup".pk;

-- ==== Les niveaux d'expertise
SELECT tag.name, el.name as expertiseLevel 
FROM pccc_schema.expertise ex, pccc_schema."expertiseLevel" el, pccc_schema.tag tag
where ex."expertiseLevelFK" = el.pk AND ex.tag = tag.pk;

-- ==== Les compétences des resources
SELECT rs.firstname, rs.lastname, tag.name as tag, el.name as expertiseLevel
FROM pccc_schema.expertise ex, pccc_schema."expertiseLevel" el, pccc_schema.tag tag, pccc_schema.resource rs 
where ex."expertiseLevelFK" = el.pk AND ex.tag = tag.pk AND ex."resourceFK" = rs.pk;

-- ==== tags concernés par une contribution
SELECT tag.name as tag, "tagGroup".name as tagGroup, contrib.synopsis, contrib.interest
FROM pccc_schema.tag, pccc_schema."tagGroup", pccc_schema.tag_x_contribution txc, pccc_schema.contribution contrib
where tag."tagGroup"="tagGroup".pk AND txc.tag_fk=tag.pk and txc.contribution_fk=contrib.pk;
