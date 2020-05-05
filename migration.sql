CREATE EXTENSION "uuid-ossp";

INSERT INTO public.authorities
(id_authority, active, created, deleted, external_id, modified, uuid, name)
VALUES(1, true, (select current_timestamp), false, null, null, (select uuid_generate_v4()), 'SUPERADMIN');

INSERT INTO public.authorities
(id_authority, active, created, deleted, external_id, modified, uuid, name)
VALUES(2, true, (select current_timestamp), false, null, null, (select uuid_generate_v4()), 'CUSTOMER');

INSERT INTO public.authorities
(id_authority,active, created, deleted, external_id, modified, uuid, name)
VALUES(3, true, (select current_timestamp), false, null, null, (select uuid_generate_v4()), 'ADMIN');


INSERT INTO public.users
(id_user, active, created, deleted, external_id, modified,email, uuid, "password", username, role)
VALUES(1, true, (select current_timestamp), false, NULL, NULL, 'maiaramartins093@gmail.com' ,  (select uuid_generate_v4()),  '$2a$10$AtGs7FzCL1B68SMr.Ju2VuaBrPTfutLtipTDR2Gt.peI1LC.OFkQ2', 'maiaramartins093@gmail.com', 'SUPERADMIN');

INSERT INTO public.administrators
(id_administrator, active, created, deleted, external_id, modified, uuid, "name", user_uuid)
VALUES(1, true, (select current_timestamp), false, NULL, NULL, (select uuid_generate_v4()), 'maiara', (select uuid from public.users where username='maiaramartins093@gmail.com'));

INSERT INTO public.user_authorities
(user_uuid, authority_uuid)
VALUES((select uuid from public.users where username='maiaramartins093@gmail.com'), (select uuid from public.authorities where name='SUPERADMIN'));]






