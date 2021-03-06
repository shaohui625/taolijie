alter table taolijie.job_post drop column post_time;
alter table taolijie.job_post drop column expired_time;
alter table taolijie.job_post add column expired_time DATETIME;
alter table taolijie.job_post add column post_time DATETIME;

alter table taolijie.second_hand_post drop column expired_time;
alter table taolijie.second_hand_post drop column post_time;
alter table taolijie.second_hand_post add column expired_time DATETIME;
alter table taolijie.second_hand_post add column post_time DATETIME;

alter table taolijie.resume drop column created_time;
alter table taolijie.resume add column created_time DATETIME;


alter table taolijie.review drop column post_id;
alter table taolijie.review add column sh_post_id int;
alter table taolijie.review add column job_post_id int;
