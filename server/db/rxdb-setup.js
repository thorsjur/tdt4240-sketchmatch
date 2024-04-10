import { createRxDatabase } from "rxdb";
import { getMemorySyncedRxStorage } from 'rxdb-premium/plugins/storage-memory-synced';
import { getRxStorageFoundationDB } from 'rxdb/plugins/storage-foundationdb';
import { guessesSchema } from "./schemas/guessesSchema";

/**
 * As of now, the database is configured with FoundationDB to provide persistant storage.
 * Could be worth checking whether it is easier just to write the data to a file.
 */
const db = await createRxDatabase({
    name: 'sketchmatchdb',
    storage: getMemorySyncedRxStorage({
        storage: getRxStorageFoundationDB({
            apiVersion: 620,
            clusterFile: '/usr/local/etc/foundationdb/fdb.cluster'
        })
    }),
    // password: 'password', <-- This is optional, only for use if we have encryption. password must be at least 12 characters
    eventReduce: true,
    ignoreDuplicate: true,
});

await db.addCollections({
    table: {
        schema: guessesSchema,
    }
});