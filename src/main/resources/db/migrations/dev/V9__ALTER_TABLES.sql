ALTER TABLE public.mensagem 
RENAME COLUMN data_envio TO created;

ALTER TABLE public.mensagem 
ADD COLUMN remetente_messages_deleted BOOLEAN DEFAULT FALSE,
ADD COLUMN destinatario_messages_deleted BOOLEAN DEFAULT FALSE;